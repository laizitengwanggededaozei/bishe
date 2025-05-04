window.addEventListener('DOMContentLoaded', function() {
    var imageInput = document.getElementById('image');
    var previewArea = document.getElementById('imageView');
    var imageUrlInput = document.getElementById('imageUrl');

    // 清除 imageView 中的所有元素
    function clearPreviewArea() {
        previewArea.innerHTML = '';
    }

    // 根据 imageUrlInput 中的图片地址，将图片插入到 imageView 中
    function insertImageFromUrl() {
        var imageUrl = imageUrlInput.value;
        if (imageUrl) {
            var img = document.createElement('img');
            img.src = imageUrl;
            img.style.width = '500px';
            clearPreviewArea(); // 清除现有的预览图片
            previewArea.appendChild(img);
        }
    }

    // 添加事件监听器，当 input[type="file"] 中的文件发生变化时触发
    imageInput.addEventListener('change', function(e) {
        var file = e.target.files[0];
        if (file) {
            var reader = new FileReader();
            reader.onloadend = function() {
                var img = document.createElement('img');
                img.src = reader.result;
                img.style.width = '500px';
                clearPreviewArea();
                previewArea.appendChild(img);
            }
            if (file.type.match('image.*')) {
                reader.readAsDataURL(file);
            } else {
                previewArea.textContent = '请选择一个图片文件';
            }
        } else {
            clearPreviewArea();
        }
    });

    // 在页面加载完毕后，根据 imageUrlInput 的值显示预览图片
    window.addEventListener('load', function() {
        insertImageFromUrl();
    });
});