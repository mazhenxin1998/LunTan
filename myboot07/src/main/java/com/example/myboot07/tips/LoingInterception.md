####   拦截处理 
######  默认跳转到index
~~~

    1. /commentUpdate  用户对问题进行一级评论
        如果当前用户未登录,那么就不能发布评论() 
    2. /saveSecondComment  用户对问题的二级评论 
        如果当前用户未登录,则发布评论失败 
    3. /update  对问题的修改 
        如果当前用户未登录,那么就应该修改失败 
    4. /publish  跳转到提问页面
        如果当前用户未登录,那么就应该跳转失败(或者跳转到index)
    5. /doquestion  用户上传问题 
        如果当前用户未登录,那么就应该提示用户登录(或者跳转到index)
    6. /dealWithMyQuestion  当前用户的  我的问题页面  或者 我的最新回复页面 
        如果当前用户未登录,那么就直接跳转到index
    7. /toPersonCenter ： 跳转到我的个人中心页面 
        如果用户未登录,那么直接跳转到index
    8. /logout : 用户退出 
        如果用户未登录,那么就直接跳转到index
    
~~~

#### 异常处理 
~~~

~~~