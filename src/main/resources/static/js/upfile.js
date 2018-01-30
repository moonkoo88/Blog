var fileManager = (function(){

    var getAll  = function(obj, callback){
        console.log("get All....");

        $.getJSON('/file/'+obj,callback );

    };

    var remove = function(obj, callback){

        console.log("remove........");

        $.ajax({
            type:'delete',
            url: '/file/'+ obj.bno+"/" + obj.id,
            dataType:'json',
            beforeSend : function(xhr){
                xhr.setRequestHeader(obj.csrf.headerName, obj.csrf.token);
            },
            contentType: "application/json",
            success:callback
        });
    };

    return {
        getAll: getAll,
        remove: remove
    }

})();