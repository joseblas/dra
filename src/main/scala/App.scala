package example

import com.hazelcast.client.HazelcastClient
import com.hazelcast.client.config.ClientConfig

object App2 {
  def main(args: Array[String]) {
    val hzConfig = new ClientConfig()


    println("connecting...")
    hzConfig.getNetworkConfig.addAddress("10.240.224.171:5701")
    val hzConnection = HazelcastClient.newHazelcastClient(hzConfig)
    println("connected")

//    hzConnection.getCluster.getMembers.forEach(s => println(s.toString))

    val hzMap = hzConnection.getMap("heatmap-incidents")


//    hzMap.put("E2","EE")

    println("Hello basic-project!  " + hzMap.size())

//    val it = hzMap.values().iterator()
//    while(it.hasNext){
//      println("Values " + it.next())
//    }

    val kk = hzMap.keySet().iterator()
    while(kk.hasNext){
      val key = kk.next().asInstanceOf[String]
      println("Keys " + key + " " +hzMap.get(key))
    }
    hzConnection.shutdown()
  }
}
