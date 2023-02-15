


const Util = {

    /**
     * ajax통신
     * @param param method, url, async..
     * @param callback 비동기 방식일때 실행할 callback함수
     * @returns {*}
     */
    getAjax(param, callback){
            const httpRequest = new XMLHttpRequest();
            httpRequest.open(param.method, param.url, param.async);
            if(param.async) httpRequest.responseType = "json";

            if(param.method == 'post'){
                httpRequest.setRequestHeader('Content-Type', 'application/json');
                httpRequest.setRequestHeader('charset', 'UTF-8');
                httpRequest.send(JSON.stringify(param.param));
            }else{
                httpRequest.send();
            }

            if(!param.async){
                const response = JSON.parse(httpRequest.response);
                if(200>response.code||299<response.code){
                    alert(response.message);
                    return;
                }

                return response.data;
            }else {
                httpRequest.onload = function(e){
                    callback(httpRequest.response);
                }
            };
    },

    addMinusChar(param){
        return param.substring(0, 4) + "-" + param.substring(4, 6) + "-" + param.substring(6, 8)
    },

    removeMinusChar(param){
        return param.replaceAll('-', '');
    },

    setForm(){
        // 지역정보 세팅
        let param = {
            method : 'GET',
            url:'/locations',
            async:true,
        }

        this.getAjax(param, function(result){
          if(result.code == 200){
              let str = "<option value=\"\">=== 선택 ===</option>";
              const size = result.data.length;
              for (let i = 0; i < size; i++) {
                  const location = result.data[i];
                  str += "<option value='"+location.locationNo+"'>"+ location.name+"</option>"
              }
              document.querySelector('#location').innerHTML = str;
          }else{
              alert(result.message);
          }
        });

        param.url = '/yesterdaysDate'
        // 어제날짜 세팅
        this.getAjax(param, function (result){
            if(result.code == 200){
                document.querySelector('input[type=\'date\']').value = Util.addMinusChar(result.data);
            }else{
                alert(result.message);
            }
        })
    },
    /**
     * 상태값을 받아 아이콘을 반환 해주는 함수
     * @param sky 하늘 상태값
     */
    getIcon(sky){
        sky = Number(sky);
        if(0 <= sky&& sky<=5){
            return "<i class=\"fa-regular fa-sun text-fcst \"></i>"
        }else if(6 <= sky&& sky<=8){
            return "<i class=\"fa-solid fa-cloud-sun text-fcst \"></i>"
        }else{
            return "<i class=\"fa-solid fa-clouds text-fcst\"></i>"
        }
    }


}