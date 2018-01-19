package commonUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.multipart.MultipartFile;



/**
 * 웹상에서 다운로드 작업을 수행하는 유틸리티
 * 
 * @author 
 * 
 */
public class FileUtil {



	/**
	 * 생성자 - 객체 생성 불가
	 */
	private FileUtil() {
		// do nothing;
	}

	/**
	 * 지정된 파일을 다운로드 한다.
	 * 
	 * @param request
	 * @param response
	 * @param file
	 *           다운로드할 파일
	 * 
	 * @throws ServletException
	 * @throws IOException
	 */
	/*public static void download(HttpServletRequest request, HttpServletResponse response, File file)
	throws ServletException, IOException {
		
		//String a = null;
		//if (a == null) {
		//	throw new IOException("다운로드 에러발생1.......!");
		//}
		
		String mimetype = request.getSession().getServletContext().getMimeType(file.getName());

		if (file == null || !file.exists() || file.length() <= 0 || file.isDirectory()) {
			throw new IOException("파일 객체가 Null 혹은 존재하지 않거나 길이가 0, 혹은 파일이 아닌 디렉토리입니다.");
		}

		InputStream is = null;

		try {
			is = new FileInputStream(file);
			download(request, response, is, file.getName(), file.length(), mimetype);			
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
	}

	*//**
	 * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
	 * 
	 * @param request
	 * @param response
	 * @param is
	 *            입력 스트림
	 * @param filename
	 *            파일 이름
	 * @param filesize
	 *            파일 크기
	 * @param mimetype
	 *            MIME 타입 지정
	 * @throws ServletException
	 * @throws IOException
	 *//*
	public static void download(HttpServletRequest request, HttpServletResponse response, InputStream is,
			String filename, long filesize, String mimetype) throws ServletException, IOException {

		*//** 다운로드 버퍼 크기 *//*
		final int BUFFER_SIZE = 8192; // 8kb
		*//** 문자 인코딩 *//*
		final String CHARSET = "utf-8";

		String mime = mimetype;


		if (mimetype == null || mimetype.length() == 0) {
			mime = "application/octet-stream;";
		}


		byte[] buffer = new byte[BUFFER_SIZE];

		response.setContentType(mime + "; charset=" + CHARSET);

		// 아래 부분에서 euc-kr 을 utf-8 로 바꾸거나 URLEncoding을 안하거나 등의 테스트를
		// 해서 한글이 정상적으로 다운로드 되는 것으로 지정한다.
		String userAgent = request.getHeader("User-Agent");

		if (userAgent.indexOf("MSIE 5.5") > -1) { // MS IE 5.5 이하
			response.setHeader("Content-Disposition", "filename=" + URLEncoder.encode(filename, "UTF-8") + ";");
		} else if (userAgent.indexOf("MSIE") > -1) { // MS IE (보통은 6.x 이상 가정)
			response.setHeader("Content-Disposition", "attachment; filename="
					+ java.net.URLEncoder.encode(filename, "UTF-8") + ";");
		} else { //  모질라나 오페라
			response.setHeader("Content-Disposition", "attachment; filename="
					+ new String(filename.getBytes(CHARSET), "latin1") + ";");
		}

		// 파일 사이즈가 정확하지 않을때는 아예 지정하지 않는다.
		if (filesize > 0) {
			response.setHeader("Content-Length", "" + filesize);
		}

		BufferedInputStream fin = null;
		BufferedOutputStream outs = null;

		try {
			fin = new BufferedInputStream(is);
			outs = new BufferedOutputStream(response.getOutputStream());
			int read = 0;
			
			while ((read = fin.read(buffer)) != -1) {
				outs.write(buffer, 0, read);
			}			
		} finally {
			try {
				outs.close();
			} catch (Exception ex1) {
			}

			try {
				fin.close();
			} catch (Exception ex2) {

			}
		} // end of try/catch
	}*/


	/**
	 * JSP에서 스트러츠 FormFile 객체로 넘어온 파일을 업로드한다.
	 * 저장된 파일명은 원래의 파일명 앞에 연월일_시분초를 붙여서 저장된다. 
	 * 저장 후  파일명  리턴, JSP화면에서  파일을 선택하지 않은 경우  ""를 리턴
	 * 
	 * @param request
	 * @param response
	 * @param formFile
	 *            업로드할 스트러츠 폼파일 객체
	 * @param upload_path 파일의 저장될 경로
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String upload(HttpServletRequest request, HttpServletResponse response, MultipartFile formFile, String uploadPath)
	throws ServletException, IOException {
		
		//String a = null;
		//if (a == null) {
		//	throw new IOException("업로드 에러발생2.......!");
		//}
		
		String fileName = formFile.getOriginalFilename();  /*getFileName();*/
		DateUtil date = new DateUtil();
		//String strDate = date.getCurrentDateString();
		//String strTime = date.getCurrentTimeString();
		String newFileName = "\\"+fileName;

		if(fileName != null && !fileName.equals("")){//업로드 파일 선택 시 
			File targetDirectory = new File(uploadPath);
			FileOutputStream fos = null;
			InputStream is = null;
			File uploadFile = null;

			try {
				byte[] buffer = new byte[8192];
				if(!targetDirectory.exists()){
					targetDirectory.mkdirs();
				}					

				uploadFile = new File(targetDirectory, newFileName);					
				fos = new FileOutputStream(uploadFile);//파일 쓰기
				int numRead = 0;
				is = formFile.getInputStream();

				do {
					numRead = is.read(buffer);
					if(numRead > 0){
						fos.write(buffer, 0, numRead);
					}
				} while(numRead != -1) ;

				/*fos.close();
				is.close();*/
			}finally {
				try {
					fos.close();
				} catch (Exception ex1) {
				}

				try {
					is.close();
				} catch (Exception ex2) {

				}
			} // end of try/catch
			
			return newFileName;
		}else{//업로드 파일 비선택 시					
			return "";
		}
		//파일 업로드관련 끝	
	}
	
	public static String upload_goods(HttpServletRequest request, HttpServletResponse response, 
			MultipartFile formFile, String uploadPath, String goodsFileName)
	throws ServletException, IOException {
		
		//String a = null;
		//if (a == null) {
		//	throw new IOException("업로드 에러발생2.......!");
		//}
		
		String fileName = formFile.getOriginalFilename();
		DateUtil date = new DateUtil();
		//String strDate = date.getCurrentDateString();
		//String strTime = date.getCurrentTimeString();
		String newFileName = goodsFileName;
		if(fileName != null && !fileName.equals("")){//업로드 파일 선택 시 
			File targetDirectory = new File(uploadPath);
			FileOutputStream fos = null;
			InputStream is = null;
			File uploadFile = null;
			try {
				byte[] buffer = new byte[8192];
				if(!targetDirectory.exists()){
					targetDirectory.mkdirs();
				}					

				uploadFile = new File(targetDirectory, newFileName);					
				fos = new FileOutputStream(uploadFile);//파일 쓰기
				int numRead = 0;
				is = formFile.getInputStream();

				do {
					numRead = is.read(buffer);
					if(numRead > 0){
						fos.write(buffer, 0, numRead);
					}
				} while(numRead != -1) ;

				fos.close();
				is.close();
			}finally {
				try {
					fos.close();
				} catch (Exception ex1) {
				}

				try {
					is.close();
				} catch (Exception ex2) {

				}
			} // end of try/catch
			
			return newFileName;
		}else{//업로드 파일 비선택 시						
			return "";
		}
		//파일 업로드관련 끝	
	}
	
	/**
	 * 삭제할 파일 경로와 삭제할 파일명을 받아 해당 파일이 존재하면 삭제하고 결과메시지를 리턴한다.
	 * 
	 * @param request
	 * @param response
	 * @param path 삭제할 파일이 위치한 경로
	 * @param fileName 삭제할 파일명
	 * @throws ServletException
	 * @throws IOException
	 */
	public static String delete(HttpServletRequest request, HttpServletResponse response, String path, String fileName)
	throws ServletException, IOException {
		
		String rtMsg="";
				
		if(fileName!=null && !"".equals(fileName)){
			String deleteFileUrl = path+fileName;

			File delFile = new File(deleteFileUrl);
			if(delFile.delete()){
				rtMsg="SUCCESS";
			}else{
				rtMsg="FAIL";				
			}
		}
		
		return rtMsg;
	}
	
	
	public static String uploadExcel(HttpServletRequest request, HttpServletResponse response, MultipartFile formFile, String uploadPath)
			throws ServletException, IOException {
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddssSSS");
			    Calendar calendar = Calendar.getInstance();
			    String dataFormat = dateFormat.format(calendar.getTime());
				
				String fileName = formFile.getOriginalFilename();  /*getFileName();*/
				DateUtil date = new DateUtil();
				
//				-------------------------------------
				int last = fileName.lastIndexOf(".");
				String realNm = fileName.substring(0, last); //확장명을 뺀 파일 명
				String ext = fileName.substring(last); //확장명

				String addDateFormat = realNm + "_"+ dataFormat; //날짜를 더한 유니크 한 이름을 생성 (Db file_nm에 Insert)
				String uploadFnm = addDateFormat + ext;
				
				String newFileName = "/"+uploadFnm;
//				-------------------------------------

				if(uploadFnm != null && !uploadFnm.equals("")){//업로드 파일 선택 시 
					//System.out.println(uploadPath);
					File targetDirectory = new File(uploadPath);
					FileOutputStream fos = null;
					InputStream is = null;
					File uploadFile = null;

					try {
						byte[] buffer = new byte[8192];
						if(!targetDirectory.exists()){
							targetDirectory.mkdirs();
						}					

						uploadFile = new File(targetDirectory, newFileName);					
						fos = new FileOutputStream(uploadFile);//파일 쓰기
						int numRead = 0;
						is = formFile.getInputStream();

						do {
							numRead = is.read(buffer);
							if(numRead > 0){
								fos.write(buffer, 0, numRead);
							}
						} while(numRead != -1) ;

						/*fos.close();
						is.close();*/
					}finally {
						try {
							fos.close();
						} catch (Exception ex1) {
						}

						try {
							is.close();
						} catch (Exception ex2) {

						}
					} // end of try/catch
					
					return newFileName;
				}else{//업로드 파일 비선택 시					
					return "";
				}
				//파일 업로드관련 끝	
			}
	
		
	/*	uploadFilePath - 파일을 제외 한 파일 경로
		dateAddNm - 유니크한 파일 명 (date값을 붙인 파일 명)
		forUserNm - 실제 사용자에게 보여 질 파일 명(date값을 제외 한 파일 명)
	*/
	@SuppressWarnings("static-access")
	public static void fileDownload(HttpServletResponse response, String uploadFilePath, String dateAddNm, String forUserNm)
			throws ServletException, IOException {
		
			
		
			String uploadFilePath2 = uploadFilePath + dateAddNm;
			//System.out.println(uploadFilePath2);
			String uploadFilePath3 = uploadFilePath2.replace("\\", "/");
			//System.out.println(uploadFilePath3);
			
			String ko = new String(forUserNm.getBytes("euc-kr"),"iso-8859-1");
			//File file = new File(uploadFilePath2,dateAddNm);
			File file = new File(uploadFilePath3 + File.separator);
			//System.getProperty(file.separator);
			//System.out.println(file.toString());
			FileInputStream fin = new FileInputStream(file);
	        int ifilesize = (int)file.length();
	        byte b[] = new byte[ifilesize];
	        
	        response.setContentLength(ifilesize);
	        response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment; filename="+ko+";");
	        response.setHeader("content-Transfer-Encoding","binary");
			response.setHeader("Pragma","no-cache;");
			response.setHeader("Expires","-1");
	
	        ServletOutputStream out = response.getOutputStream();
	        fin.read(b);
	        out.write(b,0,ifilesize);
	        out.close();
	        fin.close(); 
			
		}
}
