


const Util = {




    getAjax(url, param, method, callback){
            const httpRequest = new XMLHttpRequest();
            httpRequest.open(method, url);
            httpRequest.responseType = "json";
            if(method == 'post'){
                httpRequest.setRequestHeader('Content-Type', 'application/json');
                httpRequest.send(JSON.stringify(param));
            }else{
                httpRequest.send();
            }

            httpRequest.onload = () => {
                callback(httpRequest.response);
            }
    },


    setForm(){
        // 지역정보 세팅
        this.getAjax('/locations', '', 'GET', function(result){
          if(result.code == 200){
              let str = "<option value=\"none\">=== 선택 ===</option>";
              const size = result.data.length;
              for (let i = 0; i < size; i++) {
                  const location = result.data[i];
                  str += "<option data-value='{\"fcstCoordinate\":"+location.fcstCoordinate+", \"weatherCoordinate\":"+location.weatherCoordinate+"}'>"+ location.name+"</option>"
              }
              document.querySelector('#location').innerHTML = str;
          }else{
              alert(result.message);
          }
        });

        // 어제날짜 세팅
        this.getAjax('/yesterdaysDate', '', 'GET', function (result){
            if(result.code == 200){
                document.querySelector('input[type=\'date\']').value = result.data.substring(0, 4) + "-" + result.data.substring(4, 6) + "-" + result.data.substring(6, 8)
            }else{
                alert(result.message);
            }
        })
    },




}