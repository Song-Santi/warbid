$('.box_Slidemain').slick({
    dots: false,
    infinite: true,
    speed: 500,
    fade: true,
    cssEase: 'linear',
    prevArrow:"<button type='button' class='slick-prev pull-left'><i class='fa fa-angle-left' aria-hidden='true'></i></button>",
    nextArrow:"<button type='button' class='slick-next pull-right'><i class='fa fa-angle-right' aria-hidden='true'></i></button>"
  });

  $('.slide_Product').slick({
    infinite: true,
    slidesToShow: 4,
    slidesToScroll: 1,
    dots: true,
    arrows: false
  });

  $('.box_Diffproduct').slick({
    infinite: true,
    slidesToShow: 4,
    slidesToScroll: 1,
    dots: true,
    arrows: false
  });

  $('.slide_Ourpartners').slick({
    infinite: true,
    slidesToShow: 6,
    slidesToScroll: 1,
    arrows: false,
    autoplay: true,
    autoplaySpeed: 3000
  });

  $('.main_Img').slick({
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
    fade: true,
    asNavFor: '.extra_Img'
  });
  $('.extra_Img').slick({
    slidesToShow: 3,
    slidesToScroll: 1,
    asNavFor: '.main_Img',
    dots: true,
    centerMode: true,
    focusOnSelect: true,
    arrows: false
  });

  function animateNumber(finalNumber, delay, startNumber = 0, callback) {
    let currentNumber = startNumber
    const interval = window.setInterval(updateNumber, delay)
    function updateNumber() {
      if (currentNumber >= finalNumber) {
        clearInterval(interval)
      } else {
        currentNumber++
        callback(currentNumber)
      }
    }
  }
  
  document.addEventListener('DOMContentLoaded', function () {
    animateNumber(4000000, 1, 0, function (number) {
      const formattedNumber = number.toLocaleString()
      document.getElementById('transaction_count').innerText = formattedNumber
    })
    
    // animateNumber(98, 50, 0, function (number) {
    //   const formattedNumber = number.toLocaleString()
    //   document.getElementById('city-count').innerText = formattedNumber
    // })
    
    // animateNumber(1500, 10, 0, function (number) {
    //   const formattedNumber = number.toLocaleString()
    //   document.getElementById('customer-count').innerText = formattedNumber
    // })
  });

// const boxesBox = $('.boxes');
// const scrollBox = $('.scroll');
// const scrollMax = boxesBox[0].scrollHeight - boxesBox[0].clientHeight;
// const scrollThumbHeigth = Math.round(getScrollThumbHeigth(boxesBox[0]));

// scrollBox.css('--dasharray', scrollThumbHeigth + 1);
// scrollBox.css('--dashoffset', getNewPosition(0));

// boxesBox.scroll(function(){
//   const scroll = boxesBox.scrollTop();
//   scrollBox.css('--dashoffset', getNewPosition(scroll));
// });

// function getNewPosition(scroll){
//   const min = scrollThumbHeigth;
//   const max = -242;

//   if (scroll === 0) { return min; }

//   if (scroll === scrollMax) { return max; }

//   const proportionalValue = min + ((max - min) * scroll) / scrollMax;
//   return proportionalValue;
// }

// function getScrollThumbHeigth(element){
//   const visibleHeigth = element.clientHeight;
//   const totalHeigth = element.scrollHeight;
//   const proportion = visibleHeigth / totalHeigth;
//   const scrollThumbHeigth = proportion * visibleHeigth;

//   return scrollThumbHeigth;
// }


  