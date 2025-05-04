$(() => {
    // 检测到修改信息成功，弹出提示toast
    if (location.search.indexOf('put-success=1') >= 0) {
        toastr.success('提交成功')
    }
    if (location.search.indexOf('delete-success=1') >= 0) {
        toastr.success('删除成功')
    }
    // 获取删除确认框对象
    const deleteConfirmModelDom = document.getElementById('neumall-data-delete-confirm-modal')
    if (deleteConfirmModelDom) {
        const deleteConfirmModal = new bootstrap.Modal(deleteConfirmModelDom)
        // 为列表中的删除按钮添加点击事件
        $('.remove-btn').click((e) => {
            $('#neumall-data-delete-input-id').val($(e.currentTarget).attr('data-id'))
            deleteConfirmModal.show()
        })
    }
    // 获取编辑框对象
    const editModelDom = document.getElementById('neumall-data-edit-modal')
    if (editModelDom) {
        const editModal = new bootstrap.Modal(editModelDom)
        // 为列表中的删除按钮添加点击事件
        $('.neumall-data-add-btn').click((e) => {
            $('#shipping-address-id').val('')
            $('#inputProvince').val('')
            $('#inputCity').val('')
            $('#inputDistrict').val('')
            $('#inputDetailAddress').val('')
            editModal.show()
        })
        $('.edit-btn').click((e) => {
            $('#shipping-address-id').val($(e.currentTarget).attr('data-id'))
            $('#inputProvince').val($(e.currentTarget).attr('data-province'))
            $('#inputCity').val($(e.currentTarget).attr('data-city'))
            $('#inputDistrict').val($(e.currentTarget).attr('data-district'))
            $('#inputDetailAddress').val($(e.currentTarget).attr('data-detail-address'))
            editModal.show()
        })
    }
})