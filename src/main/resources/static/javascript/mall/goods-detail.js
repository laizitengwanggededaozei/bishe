$(() => {
    // quill editor的bug导致生成的HTML多出来个输入框，在这里做解决处理，删除该数据库，当然也可以引入官方的JS库解决，不过没必要
    $('.ql-hidden').remove()
})