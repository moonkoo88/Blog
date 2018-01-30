package com.mkcheong.controller;

import com.mkcheong.domain.ImageFile;
import com.mkcheong.domain.UploadFile;
import com.mkcheong.domain.WebBoard;
import com.mkcheong.persistence.CustomCrudRepository;
import com.mkcheong.persistence.UploadFileRepository;
import com.mkcheong.service.UploadFileService;
import com.mkcheong.util.UploadFileUtils;
import com.mkcheong.vo.PageMaker;
import com.mkcheong.vo.PageVO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/boards/")
@Log
public class WebBoardController {

    @Autowired
    //private WebBoardRepository repo;
    private CustomCrudRepository repo;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private UploadFileService uploadFileService;

    //강제로 에러 발생
    @RequestMapping("/exceptionError")
    String throwException1() throws IllegalStateException {
        throw new IllegalStateException("에러 발생");
    }
    //강제로 에러 발생
    @RequestMapping("/databaseError1")
    String throwDatabaseException1() throws SQLException {
        throw new SQLException();
    }

    @GetMapping("/list")
    public void list(PageVO vo, Model model) {
        Pageable page = vo.makePageable(0, "bno");

        //Page<WebBoard> result = repo.findAll(repo.makePredicate(vo.getType(), vo.getKeyword()), page);
        Page<Object[]> result = repo.getCustomPage(vo.getType(), vo.getKeyword(), page);

        log.info("" + page);
        log.info("" + result);
        log.info("total page number : " + result.getTotalPages());

        model.addAttribute("result", new PageMaker(result));
    }

    @GetMapping("/register")
    public void registerGET(@ModelAttribute("vo")WebBoard vo){

    }

    @PostMapping("/register")
    public String registerPOST(@ModelAttribute("vo")WebBoard vo, MultipartHttpServletRequest mtfRequest, RedirectAttributes rttr){

        List<MultipartFile> fileList = mtfRequest.getFiles("uploadFile");
        List<UploadFile> uploadFileList = getUploadFileList(fileList);
        //첨부파일이 있고
        if(uploadFileList.size()>0) {
            vo.setUploadFiles(uploadFileList);
        }

        repo.save(vo);

        rttr.addFlashAttribute("msg", "success");

        return "redirect:/boards/list";
    }

    @GetMapping("/view")
    public void view(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model){

        log.info("BNO: "+ bno);

        repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));

    }

    @Secured(value={"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
    @GetMapping("/modify")
    public void modify(Long bno, @ModelAttribute("pageVO") PageVO vo, Model model){

        log.info("MODIFY BNO : " + bno);

        repo.findById(bno).ifPresent(board -> model.addAttribute("vo", board));

    }

    @Secured(value={"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @PostMapping("/modify")
    public String modifyPost(WebBoard board, MultipartHttpServletRequest mtfRequest, PageVO vo, RedirectAttributes rttr ){

        log.info("Modify WebBoard: " + board);

        List<MultipartFile> fileList = mtfRequest.getFiles("uploadFile");
        List<UploadFile> uploadFileList = getUploadFileList(fileList);

        repo.findById(board.getBno()).ifPresent( origin ->{

            origin.setTitle(board.getTitle());
            origin.setContent(board.getContent());

            repo.save(origin);

            //첨부파일이 있고
            if(uploadFileList.size()>0) {
                for (int i = 0; i < uploadFileList.size(); i++){
                    uploadFileList.get(i).setBoardBno(board.getBno());
                    uploadFileRepository.save(uploadFileList.get(i));
                }
            }

            rttr.addFlashAttribute("msg", "success");
            rttr.addAttribute("bno", origin.getBno());
        });

        //페이징과 검색했던 결과로 이동하는 경우
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/boards/view";
    }

    @Secured(value={"ROLE_BASIC","ROLE_MANAGER","ROLE_ADMIN"})
    @Transactional
    @PostMapping("/delete")
    public String delete(Long bno, PageVO vo, RedirectAttributes rttr ){

        log.info("DELETE BNO: " + bno);

        List<UploadFile> uploadFileList = uploadFileRepository.getUploadFileOsfBoard(bno);
        if(uploadFileList.size()>0) {
            for (int i = 0; i < uploadFileList.size(); i++) {
                try {
                    UploadFileUtils.fileDelete(uploadFileList.get(i).getFilePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        repo.deleteById(bno);

        rttr.addFlashAttribute("msg", "success");

        //페이징과 검색했던 결과로 이동하는 경우
        rttr.addAttribute("page", vo.getPage());
        rttr.addAttribute("size", vo.getSize());
        rttr.addAttribute("type", vo.getType());
        rttr.addAttribute("keyword", vo.getKeyword());

        return "redirect:/boards/list";
    }

    private List<UploadFile> getUploadFileList(List<MultipartFile> fileList) {

        List<UploadFile> uploadFileList = new ArrayList<UploadFile>();
        //첨부파일이 있고
        if(fileList.size()>0) {
            //첫번째 첨부파일 사이즈가 0 이상일 때
            if(fileList.get(0).getSize()>0){
                for (MultipartFile file : fileList) {
                    String originalName = file.getOriginalFilename();
                    //확장자 검사
                    if (!UploadFileUtils.isValidExtension(originalName)) {
                        throw new IllegalStateException("유효한 확장자가 아닙니다.");
                    }
                    try {
                        //TODO 저장..
                        UploadFile uploadedFile = uploadFileService.store(file);
                        uploadFileList.add(uploadedFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return uploadFileList;
    }

}
