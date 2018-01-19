package commonUtil;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelUtil {
	
	
	FileOutputStream fileOutputStream = null;
	public ExcelUtil(){}
	
	public void beginExcel(FileOutputStream fileOutputStream, String  excelHead)
	{
	      
	      StringBuffer buffer = new StringBuffer();
	      buffer.append("<html>");
	      buffer.append("<head>");
	      buffer.append("<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>");
	      buffer.append("");
	      buffer.append("<style type='text/css'>");
	      buffer.append("body,table,tr,td,span,input,select,textarea,div {");
	      buffer.append("font-size:9pt;");
	      buffer.append("color:#000000;");
	      buffer.append("}");
	      buffer.append("</style>");
	      buffer.append("</head>");
	      buffer.append("<body id='excelBody' leftmargin='0' topmargin='0'>");
	      buffer.append(excelHead);
	      try {
	          fileOutputStream.write(buffer.toString().getBytes());
	      } catch (IOException e) {
		        try { if(fileOutputStream!=null) fileOutputStream.close(); } catch (Exception e1) { }
	      }
	}
	
	public void endExcel(FileOutputStream fileOutputStream)
	{
		try {
			 if (fileOutputStream!=null)
			 {
				 fileOutputStream.write("</body>".getBytes());
		         fileOutputStream.write("</html>".getBytes()); 
			 }
	      } catch (IOException e) {
	          
	      }
	}

	
	public HashMap setDataExcelComp(List dataList,String headColIdx, String headNameIdx, String path )throws Exception{
		
		HashMap 	returnVal 	= new HashMap();
		HashMap	 	rowMap		= new HashMap();
		try {
			
          
      	  //String targetUploadPath =  "/temp/";
			String targetUploadPath =  path+"/temp/" ;
      	  //String fileName = snuAdm.util.file.DateUtil.getCurrentDateString("yyyymmddhhmmss");
			String fileName = "exl_file_name";
      	  String[] headIdx	= headColIdx.split("[|]");
      	  String[] headName	= headNameIdx.split("[|]");
      	// 엑셀파일의 경로와 이름을 통해 POIFSFileSystem을 객체 생성
            HSSFWorkbook workbook = new HSSFWorkbook();
            
         // 엑셀 파일을 만듬
            FileOutputStream fileOutput = new FileOutputStream(targetUploadPath+fileName+".xls");
            returnVal.put("filePath", targetUploadPath+fileName+".xls");
            returnVal.put("fileName", fileName+".xls");
         // Sheet를 만듭니다.
              HSSFSheet sheet = workbook.createSheet("Sheet");

           // 엑셀 파일에 대한 워크북 객체 생성
              // 엑셀 파일에 대한워크시트의 수를 가져온다.  
            if(dataList != null && dataList.size() > 0){
          	 
          	  HSSFRow row 	= null;
          	  HSSFCell cell = null;
	              String cellData = "";  
	           
	              for(int i=0;i<dataList.size()+1;i++){
	            	  
	            	  int cellCnt	= 0;
	            	// sheet에 행을 하나 만듭니다.
	  	              row = sheet.createRow((short)i);
	  	              
	  	              
	  	            if(i == 0){ // 첫 ROW는 입력받은 헤더 값으로 저장		  	            	
	            	  for(int c=0;c<headName.length;c++){
	            		//입력받은 헤더 순서대로 값을 저장
	            		  cell = row.createCell(cellCnt);
	            		//  cell.setEncoding(HSSFCell.ENCODING_UTF_16);
	            		  HSSFCellStyle style = workbook.createCellStyle();
	            		  cellData = headName[c];
	            			  
	            			  
	            		//폰트 높이는 24, 폰트 종류는 Courier New, 이탈릭체로 설정한다
	        			  HSSFFont font = workbook.createFont();
	        			  font.setFontHeightInPoints((short)12);
	        			  font.setFontName("Courier New");
	        			  //font.setItalic(true);
	        			  style.setFont(font);
	        			  
	        			  style.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
	            		  style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		            		  
	            		//행에 셀을  만든 후 값 대입
			        	  cell.setCellStyle(style);
			        	  cell.setCellValue(cellData);
			        	  cellCnt++;  
	            	  }  
	            	}else {
	            			rowMap	= (HashMap)dataList.get(i-1);
	            			  for(int c=0;c<headIdx.length;c++){
	            				//입력받은 헤더 순서대로 값을 저장
			            		  
			            		  cellData = checkNull(rowMap.get(headIdx[c])).toString();
//			            		  '|'으로 값을 구분한 data가 존재함. 해당 데이터는 해당 ROW에 모두 뿌려줌
			            		  if(cellData.indexOf("|") > -1){
			            			  String[] colIdx	= cellData.split("[|]");
			            			  
			            			  for(int t = 0; t < colIdx.length; t++){
				            			  cell = row.createCell(cellCnt);
					            	//	  cell.setEncoding(HSSFCell.ENCODING_UTF_16);
					            		  
				            			//행에 셀을  만든 후 값 대입
					            		  cell.setCellValue(colIdx[t]);
					            		  cellCnt++;
			            			  }
			            		  } else {
			            			  cell = row.createCell(cellCnt);
				            	//	  cell.setEncoding(HSSFCell.ENCODING_UTF_16);
				            		  
				            		  HSSFCellStyle style = workbook.createCellStyle();
				            		  
				            		//행에 셀을  만든 후 값 대입
				            		  cell.setCellStyle(style);
				            		  cell.setCellValue(cellData);
				            		  cellCnt++;
			            		  }
	            			  }
	            	}
	              }
            }
         // 파일 생성
          workbook.write(fileOutput);
          fileOutput.close();
           
      } catch (Exception e) {
         e.printStackTrace();
         throw e;
     }
		return returnVal;
	}
	
	/**
	 * 엑셀 파일의 데이터를 Map로 담아 List로 반환 
	 *
	 */
	public List getDataExcelComp(String filePath)throws Exception{
		List excelList 	= new ArrayList();
          try {
        	  
              POIFSFileSystem fs = new  POIFSFileSystem(new FileInputStream(filePath));

              // 엑셀파일의 경로와 이름을 통해 POIFSFileSystem을 객체 생성
              HSSFWorkbook workbook = new HSSFWorkbook(fs);

              // 엑셀 파일에 대한 워크북 객체 생성
              int sheetNum = workbook.getNumberOfSheets();
             // 엑셀 파일에 대한워크시트의 수를 가져온다.

 
             for(int i =0; i<sheetNum; i++){
                 HSSFSheet sheet = workbook.getSheetAt(i);
                // 한 개의 시트에 대한 정보를 HSSFSheet형의 변수에 담아 객체를 생성한다. 
                int rows = sheet.getPhysicalNumberOfRows();
                // 시트별 몇개의 Row가 있는지 알아낸다.  
                StringBuffer sf = new StringBuffer(); // 결과를 콘솔에 출력한다. 
                 for (int j = 0; j < rows; j++) { // Row가 존재할 때까지 반복하는 for
                      HSSFRow row = sheet.getRow(j);
                      Map	 rowMap = new HashMap();
                      // 한개의 시트에 로우가 있는지 체크

                     if(row !=null){
                         int cells = row.getPhysicalNumberOfCells();
                         //한 개의 Row에 몇개의 Cell이 있는지 체크 
                             
                         for(int c = 0; c < cells; c++){ // Cell이 존재할 때까지 반복하는 for
                              HSSFCell cell = row.getCell(c); //  Row에 해당하는 Cell 객체를 생성
                              if(cell !=null){
                                   String value =null;

                                // 2011-01-19 (이은봉)
                                   // PIN 전용.. PIN 번호는 값이 문자임  ==> 문자, 숫자 다 가능해야하므로 아래문장 리마크
                                   // cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                                   
                                   
                                   switch(cell.getCellType()){
                                          case HSSFCell.CELL_TYPE_FORMULA:
                                          value = cell.getCellFormula();
                                          break;


                                         case HSSFCell.CELL_TYPE_NUMERIC:
                                        	 //날짜인경우
                                        	 if(HSSFDateUtil.isCellDateFormatted(cell)){
                                        		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
                                        		value=formatter.format(cell.getDateCellValue());
                                        	 }else{
                                        		 //실수 정수구분
                                        		 double gap= cell.getNumericCellValue()-(long)cell.getNumericCellValue();
                                        		 if(gap==0){
                                        			 // 2011-01-19 ,(int) --> (long) 으로 수정  [이은봉]
                                        			 value = String.valueOf((long)cell.getNumericCellValue()); 
                                        		 }else{
                                        			 value = String.valueOf(cell.getNumericCellValue());
                                        		 }	 
                                        	 }	 
                                        	 break;


                                         case HSSFCell.CELL_TYPE_STRING:
                                         value = cell.getStringCellValue();
                                         break;


                                        case HSSFCell.CELL_TYPE_BLANK:
                                        value = null;
                                        break;


                                        case HSSFCell.CELL_TYPE_BOOLEAN:
                                        value = String.valueOf(cell.getBooleanCellValue());
                                        break;

            
                                        case HSSFCell.CELL_TYPE_ERROR:
                                        value = "ERROR value"+cell.getErrorCellValue();
                                        break;


                                       default:
                                  }
                                 rowMap.put("cellValue"+c, value);
                                sf.append(value+"\t"); 
                              }
                         }
                       //  System.out.println("row "+j+"번째 값"+sf);
                        sf.delete(0, sf.capacity());
                        //첫번째 행의 값이 없는경우 리스트에 추가 안함
                        HSSFCell firstCell =row.getCell((int)row.getFirstCellNum());
                        if(!String.valueOf(firstCell.getCellType()).equals("3")){
                        	excelList.add(rowMap);
                        }
                    }//ifend 
                }//for end
            } 
        } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
		return excelList;
	}
	
	public static Object checkNull(Object val) {
		return  (val == null) ? "" : val;  
	}
	
	
	
	// *.xlsx 파일 업로드 관련
	public List getDataExcelComp2(String filePath)throws Exception{
		List excelList 	= new ArrayList();
          try {
        	  
              FileInputStream fs = new FileInputStream(filePath);

              // 엑셀파일의 경로와 이름을 통해 POIFSFileSystem을 객체 생성
              XSSFWorkbook workbook = new XSSFWorkbook(fs);
              
              // 엑셀 파일에 대한 워크북 객체 생성
              int sheetNum = workbook.getNumberOfSheets();
             // 엑셀 파일에 대한워크시트의 수를 가져온다. 

 
             for(int i =0; i<sheetNum; i++){
                 XSSFSheet sheet = workbook.getSheetAt(i);
                // 한 개의 시트에 대한 정보를 XSSFSheet형의 변수에 담아 객체를 생성한다. 
                int rows = sheet.getPhysicalNumberOfRows();
                // 시트별 몇개의 Row가 있는지 알아낸다. 
                StringBuffer sf = new StringBuffer(); // 결과를 콘솔에 출력한다. 
                 for (int j = 0; j < rows; j++) { // Row가 존재할 때까지 반복하는 for
                      XSSFRow row = sheet.getRow(j);
                      Map	 rowMap = new HashMap();
                      // 한개의 시트에 로우가 있는지 체크

                     if(row !=null){
                         int cells = row.getPhysicalNumberOfCells();
                         // 한 개의 Row에 몇개의 Cell이 있는지 체크 
                             
                         for(int c = 0; c < cells; c++){ // Cell이 존재할 때까지 반복하는 for
                              XSSFCell cell = row.getCell(c); // Row에 해당하는 Cell 객체를 생성
                              if(cell !=null){
                                   String value =null;

                                   // 2011-01-19 (이은봉)
                                   // PIN 전용.. PIN 번호는 값이 문자임  ==> 문자, 숫자 다 가능해야하므로 아래문장 리마크
                                   // cell.setCellType(XSSFCell.CELL_TYPE_STRING);
                                   
                                   
                                   switch(cell.getCellType()){
                                          case XSSFCell.CELL_TYPE_FORMULA:
                                          value = cell.getCellFormula();
                                          break;


                                         case XSSFCell.CELL_TYPE_NUMERIC:
                                        	//날짜인경우 
                                        	 if(HSSFDateUtil.isCellDateFormatted(cell)){
                                        		SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
                                        		value=formatter.format(cell.getDateCellValue());
                                        	 }else{
                                        		 //실수 정수구분
                                        		 double gap= cell.getNumericCellValue()-(long)cell.getNumericCellValue();
                                        		 if(gap==0){
                                        			 // 2011-01-19 ,  (int) --> (long) 으로 수정  [이은봉]
                                        			 value = String.valueOf((long)cell.getNumericCellValue()); 
                                        		 }else{
                                        			 value = String.valueOf(cell.getNumericCellValue());
                                        		 }	 
                                        	 }	 
                                        	 break;


                                         case XSSFCell.CELL_TYPE_STRING:
                                         value = cell.getStringCellValue();
                                         break;


                                        case XSSFCell.CELL_TYPE_BLANK:
                                        value = null;
                                        break;


                                        case XSSFCell.CELL_TYPE_BOOLEAN:
                                        value = String.valueOf(cell.getBooleanCellValue());
                                        break;

            
                                        case XSSFCell.CELL_TYPE_ERROR:
                                        value = "ERROR value"+cell.getErrorCellValue();
                                        break;


                                       default:
                                  }
                                 rowMap.put("cellValue"+c, value);
                                sf.append(value+"\t"); 
                              }
                         }
                     //  System.out.println("row "+j+"번째 값"+sf);
                         sf.delete(0, sf.capacity());
                         //첫번째 행의 값이 없는경우 리스트에 추가 안함
                         XSSFCell firstCell =row.getCell((int)row.getFirstCellNum());
                         if(!String.valueOf(firstCell.getCellType()).equals("3")){
                         	excelList.add(rowMap);
                         }
                     }//ifend 
                 }//for end
            } 
        } catch (Exception e) {
           e.printStackTrace();
           throw e;
       }
		return excelList;
	}

}
