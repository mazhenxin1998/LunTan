
// 处理注册请求

function register() {

    var account = $("#account").val();
    var password = $("#pswd").val();
    var name = $("#name").val();

    var index = -1;

    $().ajax({

        type : "POST",
        data : {
            "account" : account,
            "password" : password,
            "name" : name
        },
        url : "/dealWithRegister",

        beforeSend : function () {

            if (account == "" || account == null){
                layer.msg("账号不能为空,请输入您的账号!",{time : 1500,icon : 5,shift : 6});
                return false;
            }
            if (password == "" || password == null){
                layer.msg("密码不能为空,请重新输入您的密码!",{time : 1500,icon : 5,shift : 6});
                return false;
            }
            if (name == null || name ==""){
                layer.msg("用户名不能为空,请重新输入您的用户名!",{time : 1500,icon : 5 ,shift : 6});
                return false;
            }
            index = layer.load(2, {time: 10*1000});

            return true;
        },
        success : function (result) {

            layer.close(index);

            if (result.success){
                //  注册成功，跳转到首页
                window.location.href="/";
            }else {
                //  注册失败不回调 仍停留在该页面进行第二次注册
                layer.msg("注册失败,请重新注册!",{time : 1000,icon : 5,shift : 6});
            }
        },
        error : function () {
        }
    })
}

