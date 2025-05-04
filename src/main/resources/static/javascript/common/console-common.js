/**
 * 跳转到指定的页码
 * 供页面的公共pager调用
 * 函数会拆分所有get url参数，然后去掉原有的page参数，加入新的page页面，之后进行跳转
 * @param page 要跳转的页码
 */
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

$(() => {
    $('.neumall-nav-back-btn').click(() => {
        history.back()
    })
    // 将get url中的参数还原到action bar的input输入框中
    const search = location.search.split('?')[1]
    if (search) {
        const searchComponents = search ? search.split('&') : []
        searchComponents.forEach((component, index) => {
            if (component) {
                const keyValue = component.split('=')
                if (keyValue.length === 2) {
                    $('.action-bar input[name="' + keyValue[0] + '"]').val(decodeURIComponent(keyValue[1]))
                }
            }
        })
    }
    // 获取删除确认框对象
    const deleteConfirmModelDom = document.getElementById('neumall-data-delete-confirm-modal')
    if (deleteConfirmModelDom) {
        const deleteConfirmModal = new bootstrap.Modal(deleteConfirmModelDom)
        // 为列表中的删除按钮添加点击事件
        $('.neumall-data-remove-btn').click((e) => {
            $('#neumall-data-delete-input-id').val($(e.currentTarget).attr('data-id'))
            const customAction = $(e.currentTarget).attr('data-action')
            if (customAction && customAction.trim() !== '') {
                $('#neumall-data-delete-confirm-form').attr('action', customAction)
            }
            deleteConfirmModal.show()
        })
    }
})