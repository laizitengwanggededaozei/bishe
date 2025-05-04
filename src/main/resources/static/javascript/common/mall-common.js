$(() => {
    // 跳转到登录页面
    $('.register-btn').click(() => {
        location.href = '/edge/register'
    })

    // 跳转到登录页面
    $('.login-btn').click(() => {
        location.href = '/edge/login'
    })

    // 退出登录
    $('.logout-btn').click(() => {
        location.href = '/user-center/handler/logout'
    })
})

function gotoPage(page) {
    const search = location.search.split('?')[1]
    const searchComponents = search ? search.split('&') : []
    searchComponents.forEach((component, index) => {
        if (component.startsWith('page=')) {
            searchComponents.splice(index, 1)
        }
    })
    searchComponents.push('page=' + page)
    location.href = location.pathname + '?' + searchComponents.join('&')
}