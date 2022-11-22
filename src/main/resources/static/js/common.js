


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

    addMinusChar(param){
        return param.substring(0, 4) + "-" + param.substring(4, 6) + "-" + param.substring(6, 8)
    },

    removeMinusChar(param){
        return param.replaceAll('-', '');
    },

    setForm(){
        // 지역정보 세팅
        this.getAjax('/locations', '', 'GET', function(result){
          if(result.code == 200){
              let str = "<option value=\"\">=== 선택 ===</option>";
              const size = result.data.length;
              for (let i = 0; i < size; i++) {
                  const location = result.data[i];
                  const data = JSON.stringify({'fcstCoordinate':location.fcstCoordinate, 'weatherCoordinate':location.weatherCoordinate});
                  str += "<option value='"+data+"'>"+ location.name+"</option>"
              }
              document.querySelector('#location').innerHTML = str;
          }else{
              alert(result.message);
          }
        });

        // 어제날짜 세팅
        this.getAjax('/yesterdaysDate', '', 'GET', function (result){
            if(result.code == 200){
                document.querySelector('input[type=\'date\']').value = Util.addMinusChar(result.data);
            }else{
                alert(result.message);
            }
        })
    },


}