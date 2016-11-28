# AsaSystembar
      下载项目可以直接打开，但是构建会报错，找不到gradle，gradle需要你自己去下载，迅雷特别快，
      
      我们对gradle wrapper的使用方式做了一点修改。首先其默认使用方式是，先从distributionUrl中解析出声明使用的gradle版本，
      如果本地已下载过该版本，则将直接复用（本地搜寻路径由上面四行的路径声明，可以去该路径查看你本地已有的gradle版本），
      如果本地没有，则将自动从这个url下载gradle。这种想法很美好，然而在国内是个悲剧，gradle-x.zip有90MB左右的体积，其在国内没有服务器，
      所以在国内下载速度极慢，可能达到1小时+（这就是为什么用AS打开某些项目，会直接卡在那里，像卡死了一样，其实是在下载gradle）。

      我们这里的使用方式是，直接将gradle-x.zip放入当前目录，并声明distributionUrl=gradle-x.zip，这样相当于声明了下载路径为当前目录。
      而至于你如何去获取gradle-x.zip，well，迅雷，你懂的。

      ps 当前目录指的是此配置文件所在目录，即yourProject/gradle/wrapper/
      //distributionUrl=https\://services.gradle.org/distributions/gradle-2.14.1-all.zip
      distributionUrl=gradle-2.14.1-all.zip
