
def random = new Random();
def map = [:]

int MIN = params.distMin? params.distMin.toInteger():0   // 最大值
int MAX = params.distMax? params.distMax.toInteger():100 // 最小值
int mean = params.distMean? params.distMean.toInteger():50 // 数学期望值
int sd = params.distSD? params.distSD.toInteger():2   // 标注差
int nums = params.distTimes? params.distTimes.toInteger():100

MAX = Math.max(MAX, MIN)
MIN = Math.min(MAX, MIN)

if(MAX == MIN) {
  MAX = MIN + 100
}

if(nums <=0) {
  nums = 100
}

// System.out.println "MIN:$MIN"
// System.out.println "MAX:$MAX"
// System.out.println "mean:$mean"
// System.out.println "sd:$sd"
// System.out.println "nums:$nums"

nums.times {

   def gaussian = MIN - 1

   while(gaussian < MIN || gaussian > MAX) {
    gaussian = random.nextGaussian(); // mean 0.0, standard deviation 1.0
    // transform
    gaussian = (int)(sd * gaussian) + mean
   }

  //  System.out.println "runs[$it]=$gaussian"

  if(map[gaussian]) {
    map[gaussian] += 1
  } else {
    map[gaussian] = 1
  }
}

// System.out.println map

def keys = map.keySet().sort()

def dataLabels = (keys[0]..keys[-1]).collect { it.toString() }

def dataList = (keys[0]..keys[-1]).collect { map[it]?:0 }

html.html {
    head {
        title 'Gaussian Distribution Test'
        script src:'//cdn.bootcss.com/Chart.js/1.0.2/Chart.min.js'
    }
    body {
      h1 '（伪）正态分布研究'
      canvas id:'myChart', width: 800, height: 500
      form (method:'POST') {
        label ('最小值MIN') { input(type:'number', name:'distMin', placeholder:'最小值', value:"$MIN", required:'required') }
        label ('最大值MAX') { input(type:'number', name:'distMax', placeholder:'最大值', value:"$MAX", required:'required') }
        label ('数学期望值') { input(type:'number', name:'distMean', placeholder:'数学期望', value:"$mean", required:'required') }
        label ('标准差') { input(type:'number', name:'distSD', placeholder:'标准差', value:"$sd", required:'required') }
        label ('样本总量') { input(type:'number', name:'distTimes', placeholder:'样本总量', value:"$nums", required:'required') }
        button(type:'submit', '提交')
      }
      script {
        mkp.yield 'var data ='
        json {
          labels dataLabels
        	datasets ([[
          			fillColor : "rgba(151,187,205,0.5)",
          			strokeColor : "rgba(151,187,205,1)",
          			pointColor : "rgba(151,187,205,1)",
          			pointStrokeColor : "#fff",
          			data : dataList
      		]])
        }
      }
      script {
        mkp.yieldUnescaped '''
        var options = {

        	//String - Colour of the scale line
        	scaleLineColor : "rgba(0,0,0,.5)",

        	//String - Colour of the grid lines
        	scaleGridLineColor : "rgba(0,0,0,.1)",

        	//Boolean - Whether to show a dot for each point
        	pointDot : false,

        };

          var ctx = document.getElementById("myChart").getContext("2d");
          new Chart(ctx).Line(data, options);
        '''
      }
    }
}
