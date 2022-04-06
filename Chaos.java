
import sun.misc.BASE64Encoder;

/**
 * 
 * @author liudi
 *	liudi@jzcreater.com
 */
public class Chaos {
	public static void main(String args[]){
		try {
			String key = "88188";
		    String src = "{this is my test of {chaos},lets see what will happen}";
		    //use chaos direct
		    String enc = new String(chaosEnc(src.getBytes(),key));
		    System.out.println("enc result===>\n"+enc);
		    System.out.println("dec ===>\n"+new String(chaosDec(enc.trim().getBytes(),key)));
		    System.out.println("use wrong key===>\n"+new String(chaosDec(enc.getBytes(),"dsed")));
		    
		    //use chaos with base64
            src = new BASE64Encoder().encode(src.getBytes());
            System.out.println("base64===>\n"+src);
            enc = new String(chaosEnc(src.getBytes(),key));
		    System.out.println("enc result===>\n"+enc);
		    System.out.println("dec ===>\n"+new String(chaosDec(enc.trim().getBytes(),key)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//encryption ���ע�⣺��Կ������ͬһ���ַ����ɣ���������4λ������Ϊ6λ���ϣ�Դ�ĳ��ȱ�������Ϊ8λ���������50λ��Դ�Ķ�base64������м���
	public static byte[] chaosEnc(byte[] data,String key){
		if(data.length<8){//���Ȳ������
			return data;
		}
		byte[] keyBytes = key.getBytes();
		int length_key = keyBytes.length;
		byte[] encresult;
		if(data.length%7>0){
			encresult = new byte[(data.length/7+1)*7];
		}else{
			encresult = new byte[data.length];
		}
		System.arraycopy(data, 0, encresult, 0, data.length);
		int chaosDataNum = encresult.length/7;
		//������Կ���������������������ɸ�����Ҫ����
		int membersInGroup = length_key/2;
		int chaosgroups = 0;
		chaosgroups = chaosDataNum/membersInGroup;
		if(chaosDataNum%(membersInGroup)>0){
			chaosgroups++;
		}
		int i=0;
		byte[] encdata1 = new byte[7],encdata2 = new byte[7];
		while(i<chaosgroups){
			int groupcusor = 0;//ָ��ǰ��Ա���α�
			int dataStartOffset = i*membersInGroup;
			for(int j=0;j<length_key;j++){
				System.arraycopy(encresult, (dataStartOffset+groupcusor)*7, encdata1, 0, 7);
				if(groupcusor+1<membersInGroup && (dataStartOffset+groupcusor+1)*7<encresult.length){
					System.arraycopy(encresult, (dataStartOffset+groupcusor+1)*7, encdata2, 0, 7);
				}else{
					System.arraycopy(encresult, dataStartOffset*7, encdata2, 0, 7);
				}
				chaosrule(encdata1,encdata2,keyBytes[j]);
				System.arraycopy(encdata1, 0, encresult, (dataStartOffset+groupcusor)*7, 7);
				if(groupcusor+1<membersInGroup  && (dataStartOffset+groupcusor+1)*7<encresult.length){
					groupcusor++;
				}else{
					groupcusor=0;
				}
				System.arraycopy(encdata2, 0, encresult, (dataStartOffset+groupcusor)*7, 7);
			}
			i++;
		}
		return encresult;
	}
	
	//decryption
	public static byte[] chaosDec(byte[] data,String key){
		if(data.length<8){//���Ȳ������
			return data;
		}
		byte[] keyBytes = key.getBytes();
		int length_key = keyBytes.length;
		byte[] deccresult;
		if(data.length%7>0){
			deccresult = new byte[(data.length/7+1)*7];
		}else{
			deccresult = new byte[data.length];
		}
		
		System.arraycopy(data, 0, deccresult, 0, data.length);
		int chaosDataNum = deccresult.length/7;
		//������Կ���������������������ɸ�����Ҫ����
		int membersInGroup = length_key/2;
		int chaosgroups = 0;
		chaosgroups = chaosDataNum/membersInGroup;
		if(chaosDataNum%(membersInGroup)>0){
			chaosgroups++;
		}
		int i=0;
		byte[] encdata1 = new byte[7],encdata2 = new byte[7];
		while(i<chaosgroups){
			int groupcusor = 0;//ָ��ǰ��Ա���α�
			int dataStartOffset = i*membersInGroup;
			int dataEndInGroup = dataStartOffset+membersInGroup-1;//���ڷ�����ܲ���Ա����Ҫȷ�����һ����Ա��λ��
			int groupcusorEnd = membersInGroup-1;
			if(dataEndInGroup>chaosDataNum-1){
				dataEndInGroup = chaosDataNum-1;
				groupcusorEnd = dataEndInGroup - dataStartOffset;
			}
			
			
			//���������α�����λ��
			for(int j=0;j<length_key;j++){
				if(groupcusor+1<membersInGroup  && (dataStartOffset+groupcusor+1)*7<deccresult.length){
					groupcusor++;
				}else{
					groupcusor=0;
				}
			}
			
			for(int j=length_key-1;j>=0;j--){
				System.arraycopy(deccresult, (dataStartOffset+groupcusor)*7, encdata1, 0, 7);
				if(groupcusor-1>=0){//����һ����Ա�����滻
					System.arraycopy(deccresult, (dataStartOffset+groupcusor-1)*7, encdata2, 0, 7);
				}else{//��һ���Ա��Ҫ�����һ���Ա�����滻
					System.arraycopy(deccresult, dataEndInGroup*7, encdata2, 0, 7);
				}
				chaosrule(encdata1,encdata2,keyBytes[j]);
				System.arraycopy(encdata1, 0, deccresult, (dataStartOffset+groupcusor)*7, 7);
				if(groupcusor-1>=0){
					groupcusor--;
					System.arraycopy(encdata2, 0, deccresult, (dataStartOffset+groupcusor)*7, 7);
				}else{
					groupcusor=groupcusorEnd;
					System.arraycopy(encdata2, 0, deccresult, dataEndInGroup*7, 7);
				}
				
			}
			i++;
		}
		for(i=deccresult.length-1;i>0;i--){
			if(deccresult[i]!=0){
				break;
			}
		}
		byte[] realdata = new byte[i+1];
		System.arraycopy(deccresult, 0, realdata, 0, i+1);
		return realdata;
	}

	//ascii rule
	public static void chaosrule(byte[] data1,byte[] data2,byte key){
		byte temp;
		if(key>=33 && key<=47){
			temp = data2[1];
			data2[1]=data1[1];
			data1[1]=temp;
			if(key>=40){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}else if(key>=48 && key<=63){
			temp = data2[1];
			data2[1]=data1[1];
			data1[1]=temp;
			temp = data2[2];
			data2[2]=data1[2];
			data1[2]=temp;
			if(key>=56){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}else if(key>=64 && key<=79){
			temp = data2[0];
			data2[0]=data1[0];
			data1[0]=temp;
			if(key>=72){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}else if(key>=80 && key<=95){
			temp = data2[0];
			data2[0]=data1[0];
			data1[0]=temp;
			temp = data2[2];
			data2[2]=data1[2];
			data1[2]=temp;
			if(key>=88){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}else if(key>=96 && key<=111){
			temp = data2[0];
			data2[0]=data1[0];
			data1[0]=temp;
			temp = data2[1];
			data2[1]=data1[1];
			data1[1]=temp;
			if(key>=104){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}else if(key>=112 && key<=126){
			temp = data2[0];
			data2[0]=data1[0];
			data1[0]=temp;
			temp = data2[1];
			data2[1]=data1[1];
			data1[1]=temp;
			temp = data2[2];
			data2[2]=data1[2];
			data1[2]=temp;
			if(key>=120){
				temp = data2[3];
				data2[3]=data1[3];
				data1[3]=temp;
			}
		}
		if(key%2==0){
			temp = data2[6];
			data2[6]=data1[6];
			data1[6]=temp;
		}
		if(key%4==2 || key%4==3){
			temp = data2[5];
			data2[5]=data1[5];
			data1[5]=temp;
		}
		if(key%8==4 || key%8==5 || key%8==6 || key%8==7){
			temp = data2[4];
			data2[4]=data1[4];
			data1[4]=temp;
		}

	}
}

