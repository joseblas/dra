

def req = <fact version="1.0">
  <transaction operatorIssuedDate="2005-01-01T00:00:02" operatorTransactionId="1234" operatorId="sky" receivedDate="2007-04-20T11:58:24">
  <instruction version="1" type="PlaceOrder">
    <product type="OFFNET-FTTP-UFO-DATA"/>
    <product type="OFFNET-FTTP-UFO-VOICE"/>
    <order>
      <type>provide</type>
      <operatorOrderId>operatorOrderId</operatorOrderId>
      <operatorNotes>2016-05-05T00:00:00</operatorNotes>
      <orderId>111</orderId>
    </order>
    <fttpProvideInstruction operatorOrderId="operatorOrderId" operatorNotes="2016-05-05T00:00:00">
      <productBundle>
        <productSpecification productName="OFFNET-FTTP-UFO-DATA" broadbandSpeedConfiguration="UFO-50"/>
        <productSpecification productName="OFFNET-FTTP-UFO-VOICE">
          <features>
            <feature code="CallWaiting"/>
            <feature code="CallDisplay"/>
          </features>
        </productSpecification>
      </productBundle>
      <emergencyServicesDetails firstNameOrInitial="J" surname="Bloggs" premises="Flat 2, 15" thoroughfare="Lovely Street" locality="Manchester" postcode="LV1 3XX"/>
      <directoryEntryDetail type="inclusion" surname="Bloggs" firstNameOrInitial="Joe" specialEquipment="fax" specialTypeface="bold" premises="Flat 2, 15" thoroughfare="Lovely Street" locality="Manchester" postcode="LV1 3XX"/>
      <deliveryDetails uprn="12345678" requestedDeliveryDate="2015-05-05T00:00:00">
        <appointment reservationReference="X123123KHA" contactName="John" contactNumber="07805010101" hazardNotes="Beware of the dog" specialArrangements="Knock twice"/>
      </deliveryDetails>
    </fttpProvideInstruction>
  </instruction>
</transaction></fact>


println ("D "+ (req \ "fact" ).text+"D")
req \ "fact"  match {

  case <transaction /> => println("!!")
  case _ => println("Nop")
}