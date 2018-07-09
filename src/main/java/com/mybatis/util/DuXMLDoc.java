package com.mybatis.util;


import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;


public class DuXMLDoc {  
    public List xmlElements(String xmlDoc) throws DocumentException {  
    	Document doc = null;          
        doc = DocumentHelper.parseText(xmlDoc);
        Element rootElt = doc.getRootElement(); // 获取根节点
        String service = rootElt.attributeValue("service");
        String lang = rootElt.attributeValue("lang");
        //获取根节点的名称和里面的参数
        System.out.println("根节点：" + rootElt.getName()+": "+service+"--"+lang);
        //获取根节点标签中的参数
        Attribute RequestContent = rootElt.attribute("service");
        if (null != RequestContent) {
        	String RequestContentkey = RequestContent.getName();
        	String RequestContentValue = RequestContent.getValue();
        	System.out.println(RequestContentkey + ": " + RequestContentValue);
        }
        
        //head节点
        Iterator iterHead = rootElt.elementIterator("Head");        
        while (iterHead.hasNext()) {
        	Element recordHead = (Element) iterHead.next();
        	System.out.println(recordHead.getName()+"--"+recordHead.getText());
        }
        
        //body节点
        Iterator iterBody = rootElt.elementIterator("Body");
        while (iterBody.hasNext()) {
        	Element recordBody = (Element) iterBody.next();
//        	Attribute bodyid = recordBody.attribute("bodyid");
//        	if(null != bodyid){
//            	String bodykey = bodyid.getName();
//            	String bodyValue = bodyid.getValue();
//            	System.out.println(bodykey + ": " + bodyValue);
//            }
        	//获取Order标签
        	Iterator iterOrder = recordBody.elementIterator("Order");
        	while(iterOrder.hasNext()){
        		Element recordOrder = (Element)iterOrder.next();
        		//orderid
        		Attribute orderid = recordOrder.attribute("orderid");
        		String orderidKey = orderid.getName();
        		String orderidValue = orderid.getValue();
        		System.out.println(orderidKey + ": " +orderidValue);
        		//j_company
        		Attribute j_company = recordOrder.attribute("j_company");
        		String j_companyKey = j_company.getName();
        		String j_companyValue = j_company.getValue();
        		System.out.println(j_companyKey+": "+j_companyValue);
        		//j_contact
        		Attribute j_contact = recordOrder.attribute("j_contact");
        		String j_contactKey = j_contact.getName();
        		String j_contactValue = j_contact.getValue();
        		System.out.println(j_contactKey+": "+j_contactValue);
        		//j_tel
        		Attribute j_tel = recordOrder.attribute("j_tel");
        		String j_telKey = j_tel.getName();
        		String j_telValue = j_tel.getValue();
        		System.out.println(j_telKey+": "+j_telValue);
        		//j_mobile
        		Attribute j_mobile = recordOrder.attribute("j_mobile");
        		String j_mobileKey = j_mobile.getName();
        		String j_mobileValue = j_mobile.getValue();
        		System.out.println(j_mobileKey+": "+j_mobileValue);
        		//j_province
        		Attribute j_province = recordOrder.attribute("j_province");
        		String j_provinceKey = j_province.getName();
        		String j_provinceValue = j_province.getValue();
        		System.out.println(j_provinceKey+": "+j_provinceValue);
        		//j_city
        		Attribute j_city = recordOrder.attribute("j_city");
        		String j_cityKey = j_city.getName();
        		String j_cityValue = j_city.getValue();
        		System.out.println(j_cityKey+": "+j_cityValue);
        		//j_county
        		Attribute j_county = recordOrder.attribute("j_county");
        		String j_countyKey = j_county.getName();
        		String j_countyValue = j_county.getValue();
        		System.out.println(j_countyKey+": "+j_countyValue);
        		//j_address
        		Attribute j_address = recordOrder.attribute("j_address");
        		String j_addressKey = j_address.getName();
        		String j_addressValue = j_address.getValue();
        		System.out.println(j_addressKey+": "+j_addressValue);
        		//d_company
        		Attribute d_company = recordOrder.attribute("d_company");
        		String d_companyKey = d_company.getName();
        		String d_companyValue = d_company.getValue();
        		System.out.println(d_companyKey+": "+d_companyValue);
        		//d_contact
        		Attribute d_contact = recordOrder.attribute("d_contact");
        		String d_contactKey = d_contact.getName();
        		String d_contactValue = d_contact.getValue();
        		System.out.println(d_contactKey+": "+d_contactValue);
        		//d_tel
        		Attribute d_tel = recordOrder.attribute("d_tel");
        		String d_telKey = d_tel.getName();
        		String d_telValue = d_tel.getValue();
        		System.out.println(d_telKey+": "+d_telValue);
        		//d_mobile
        		Attribute d_mobile = recordOrder.attribute("d_mobile");
        		String d_mobileKey = d_mobile.getName();
        		String d_mobileValue = d_mobile.getValue();
        		System.out.println(d_mobileKey+": "+d_mobileValue);
        		//d_address
        		Attribute d_address = recordOrder.attribute("d_address");
        		String d_addressKey = d_address.getName();
        		String d_addressValue = d_address.getValue();
        		System.out.println(d_addressKey+": "+d_addressValue);
        		//express_type
        		Attribute express_type = recordOrder.attribute("express_type");
        		String express_typeKey = express_type.getName();
        		String express_typeValue = express_type.getValue();
        		System.out.println(express_typeKey+": "+express_typeValue);
        		//pay_method
        		Attribute pay_method = recordOrder.attribute("pay_method");
        		String pay_methodKey = pay_method.getName();
        		String pay_methodValue = pay_method.getValue();
        		System.out.println(pay_methodKey+": "+pay_methodValue);
        		//parcel_quantity
        		Attribute parcel_quantity = recordOrder.attribute("parcel_quantity");
        		String parcel_quantityKey = parcel_quantity.getName();
        		String parcel_quantityValue = parcel_quantity.getValue();
        		System.out.println(parcel_quantityKey+": "+parcel_quantityValue);
        		//cargo_length
        		Attribute cargo_length = recordOrder.attribute("cargo_length");
        		String cargo_lengthKey = cargo_length.getName();
        		String cargo_lengthValue = cargo_length.getValue();
        		System.out.println(cargo_lengthKey+": "+cargo_lengthValue);
        		//cargo_width
        		Attribute cargo_width = recordOrder.attribute("cargo_width");
        		String cargo_widthKey = cargo_width.getName();
        		String cargo_widthValue = cargo_width.getValue();
        		System.out.println(cargo_widthKey+": "+cargo_widthValue);
        		//cargo_height
        		Attribute cargo_height = recordOrder.attribute("cargo_height");
        		String cargo_heightKey = cargo_height.getName();
        		String cargo_heightValue = cargo_height.getValue();
        		System.out.println(cargo_heightKey+": "+cargo_heightValue);
        		//remark
        		Attribute remark = recordOrder.attribute("remark");
        		String remarkKey = remark.getName();
        		String remarkValue = remark.getValue();
        		System.out.println(remarkKey+": "+remarkValue);
        		//获取Cargo标签
        		Iterator iterCargo = recordOrder.elementIterator("Cargo");
            	while(iterCargo.hasNext()){
            		Element recordCargo = (Element)iterCargo.next();
            		//name
            		Attribute name = recordCargo.attribute("name");
            		String nameKey = name.getName();
            		String nameValue = name.getValue();
            		System.out.println(nameKey+": "+nameValue);
            		//count
            		Attribute count = recordCargo.attribute("count");
            		String countKey = count.getName();
            		String countValue = count.getValue();
            		System.out.println(countKey+": "+countValue);
            		//unit
            		Attribute unit = recordCargo.attribute("unit");
            		String unitKey = unit.getName();
            		String unitValue = unit.getValue();
            		System.out.println(unitKey+": "+unitValue);
            		//weight
            		Attribute weight = recordCargo.attribute("weight");
            		String weightKey = weight.getName();
            		String weightValue = weight.getValue();
            		System.out.println(weightKey+": "+weightValue);
            		//amount
            		Attribute amount = recordCargo.attribute("amount");
            		String amountKey = amount.getName();
            		String amountValue = amount.getValue();
            		System.out.println(amountKey+": "+amountValue);
            		//currency
            		Attribute currency = recordCargo.attribute("currency");
            		String currencyKey = currency.getName();
            		String currencyValue = currency.getValue();
            		System.out.println(currencyKey+": "+currencyValue);
            		//source_area
            		Attribute source_area = recordCargo.attribute("source_area");
            		String source_areaKey = source_area.getName();
            		String source_areaValue = source_area.getValue();
            		System.out.println(source_areaKey+": "+source_areaValue);
            	}
            	//获取AddedService标签
        		Iterator iterAddedService = recordOrder.elementIterator("AddedService");
            	while(iterAddedService.hasNext()){
            		Element recordAddedService = (Element)iterAddedService.next();
            		//name
            		Attribute name = recordAddedService.attribute("name");
            		String nameKey = name.getName();
            		String nameValue = name.getValue();
            		System.out.println(nameKey+": "+nameValue);
            		//value
            		Attribute value = recordAddedService.attribute("value");
            		if(value != null){
            			String valueKey = value.getName();
                		String valueValue = value.getValue();
                		System.out.println(valueKey+": "+valueValue);
            		}	
            		//value1
            		Attribute value1 = recordAddedService.attribute("value1");
            		if(value1 != null){
            			String value1Key = value1.getName();
                		String value1Value = value1.getValue();
                		System.out.println(value1Key+": "+value1Value);
            		}
            	}
        	}
        	Iterator iterExtra = recordBody.elementIterator("Extra");
        	while(iterExtra.hasNext()){
        		Element recordExtra = (Element)iterExtra.next();
        		Attribute e1 = recordExtra.attribute("e1");
        		if(null != e1){
                	String e1key = e1.getName();
                	String e1Value = e1.getValue();
                	System.out.println(e1key + ": " + e1Value);
                }
        		Attribute e2 = recordExtra.attribute("e2");
        		if(null != e2){
                	String e2key = e2.getName();
                	String e2Value = e2.getValue();
                	System.out.println(e2key + ": " + e2Value);
                }
        	}
		}
        return null;  
    }  
//    public static void main(String[] args) throws DocumentException{  
//        DuXMLDoc docx = new DuXMLDoc();           
//        String xmlRequest = "<?xml version=\"1.0\" encoding=\"utf-8\"?><Request service=\"OrderService\" lang=\"zh-CN\"><Head>BSPdevelop</Head><Body><Order orderid=\"TE20150104\" j_company=\"罗湖火车站\" j_contact=\"小雷\" j_tel=\"13810744\" j_mobile=\"13111744\" j_province=\"广东省\" j_city=\"深圳\" j_county=\"福田区\" j_address=\"罗湖火车站东区调度室\" d_company=\"顺丰速运\" d_contact=\"小邱\" d_tel=\"15819050\" d_mobile=\"15539050\" d_address=\"北京市海淀区中关村\" express_type=\"1\" pay_method=\"1\" parcel_quantity=\"1\" cargo_length=\"33\" cargo_width=\"33\" cargo_height=\"33\" remark=\"\"><Cargo name=\"LV1\" count=\"3\" unit=\"a\" weight=\"\" amount=\"\" currency=\"\" source_area=\"\"></Cargo><Cargo name=\"LV2\" count=\"3\" unit=\"a\" weight=\"\" amount=\"\" currency=\"\" source_area=\"\"></Cargo><AddedService name=\"COD\" value=\"3000\" value1=\"0123456789\"></AddedService><AddedService name=\"INSURE\" value=\"2304.23\"></AddedService><AddedService name=\"TDELIVERY\" value=\"20150612\" value1=\"1\"></AddedService><AddedService name=\"URGENT\"></AddedService></Order><Extra e1=\"abc\" e2=\"abc\"/></Body></Request>";
//        docx.xmlElements(xmlRequest);  
//    }  
}  