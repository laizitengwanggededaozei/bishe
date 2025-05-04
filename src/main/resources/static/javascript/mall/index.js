$(() => {
    const myCarouselElement = document.querySelector('#homeBanner')
    const carousel = new bootstrap.Carousel(myCarouselElement, {
        interval: 2000,
        touch: false
    })
    console.log('初始化轮播图', carousel)
})