package com.company;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {
    public static final int MAX_DEPTH = 5;
    public static HashSet<String> links = new HashSet<>();
    public static HashSet<String> faculties = new HashSet<>();
    public static Queue<String > pending = new LinkedList<>();
    public static String[] keywords = {"jpg","JPG", "jpeg", "JPEG" , "png", "PNG", "pdf", "PDF", "doc", "DOC", "xlsx", "ppt", "events", "#", "donations", "tnp",  "jobs", "tenders"};
    public static String[] keys = {"faculty/aero", "faculty/applied-sciences", "faculty/civil", "faculty/cse", "faculty/ee", "faculty/ece", "faculty/me", "faculty/metta", "faculty/pie"};
    public static Set<Object[]> tags = new HashSet<>();
    public static Set<Object[]> fac = new HashSet<>();
    public static void bfs(String seedUrl) throws IOException {
        pending.add(seedUrl);
        links.add(seedUrl);
        int depth = 0;
        while(depth<MAX_DEPTH && !pending.isEmpty()){
            int size=pending.size();
            depth++;
            while(size!=0){
                size--;
                String url = pending.poll();
                assert url != null;
                boolean flag1 = Arrays.stream(keys).anyMatch(url::contains);
                scrapper(url);
                try{
                    Document document = Jsoup.connect(url).get();
                    Elements linksOnPage = document.select("a[href]");
                    for(Element page: linksOnPage){
                        String crawled=page.attr("abs:href");
                        boolean flag= Arrays.stream(keywords).anyMatch(crawled::contains);
                        if(flag1 && !links.contains(crawled) && !flag && crawled.startsWith(seedUrl)){
                            faculties.add(crawled);
                        }
                        if(!links.contains(crawled) && !flag && crawled.startsWith(seedUrl)){
                            System.out.println(">> Depth: " + depth + " [" + crawled + "]");
                            links.add(crawled);
                            pending.add(crawled);
                        }

                    }
                } catch (HttpStatusException e){
                    System.out.println("Error 404...!!");
                } catch (MalformedURLException e){
                    System.out.println("The URL is Malformed...!!");
                } catch(IOException e){
                    System.out.println("The URL is invalid...!!");
                } catch (Exception e){
                    System.out.println("Invalid URL.");
                }

            }
        }
    }

    public static void scrapper(String url) throws IOException {
        try {
            Document doc = Jsoup.connect(url).get();
            Elements links = doc.select("*");
            for (Element link : links) {
                if (link.text().equals("") || link.tagName().equals(""))
                    continue;
                tags.add(new Object[]{
                        link.tagName(), link.text()});
            }
        } catch (HttpStatusException e) {
            System.out.println("Connection timed out...!!");
        } catch(Exception e){
            System.out.println("Invalid URL...!!");
        }
    }

    public static void addTagInfo(){
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet spreadsheet = workbook.createSheet(" Tag Info ");
            Row r = spreadsheet.createRow(spreadsheet.getLastRowNum()+1);
            int c = 0;
            String[] headings = {"Tags", "Text"};
            for(String head: headings){
                Cell cel =r.createCell(c++);
                cel.setCellValue(head);
            }
            r = spreadsheet.createRow(spreadsheet.getLastRowNum()+1);
            c = 0;
            for(String ignored : headings){
                Cell cel =r.createCell(c++);
                cel.setCellValue("");
            }
            for(Object[] objArr: tags){
                Row row = spreadsheet.createRow(spreadsheet.getLastRowNum()+1);
                int cellid = 0;
                for (Object obj : objArr){
                    Cell cell = row.createCell(cellid++);
                    cell.setCellValue((String)obj);
                }
            }
            try{
                FileOutputStream out = new FileOutputStream(new File("C:\\Users\\HP\\Desktop\\data.xlsx"));
                workbook.write(out);
                out.close();
            } catch (IOException e){
                System.out.println("File can't be accessed...!!");
            } catch (Exception e){
                System.out.println("Invalid access...!!");
            }
    }

    public static void addData() throws IOException {
        XSSFWorkbook workbook1 = new XSSFWorkbook();
        XSSFSheet spreadsheet1 = workbook1.createSheet(" Faculty Info ");
        Row r = spreadsheet1.createRow(spreadsheet1.getLastRowNum()+1);
        int c = 0;
        String[] headings = {"Name", "Department", "Designation", "Qualification",
                "Research Interests", "Address", "Email", "Phone Number"};
        for(String head: headings){
            Cell cel =r.createCell(c++);
            cel.setCellValue(head);
        }
        r = spreadsheet1.createRow(spreadsheet1.getLastRowNum()+1);
        c = 0;
        for(String ignored : headings){
            Cell cel =r.createCell(c++);
            cel.setCellValue("");
        }
        for(Object[] objArr: fac){
            Row row = spreadsheet1.createRow(spreadsheet1.getLastRowNum()+1);
            int cellid = 0;
            for (Object obj : objArr){
                Cell cell = row.createCell(cellid++);
                cell.setCellValue((String)obj);
            }
        }
        try{
            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\HP\\Desktop\\faculty.xlsx"));
            workbook1.write(out);
            out.close();
        } catch (IOException e){
            System.out.println("File can't be accessed...!!");
        } catch (Exception e){
            System.out.println("Invalid access...!!");
        }
    }

    public static void main(String[] args) throws IOException {
        String seedUrl = "https://pec.ac.in";
        bfs(seedUrl);
        addTagInfo();
        System.out.println("The tags info had been filled successfully.");
        for(String url: faculties){
            Document doc = Jsoup.connect(url).get();
            Element table = doc.selectFirst("tbody");
            Element heading = doc.select("div.panel-heading").first();
            if(table!=null){
                Elements rows = table.select("tr");
                Object[] objectArr = new Object[rows.size()+1];
                objectArr[0] = heading.text();
                int i=1;
                for (Element row : rows) {
                    Elements cols = row.select("td");
                    objectArr[i++] = cols.get(1).text();
                }
                fac.add(objectArr);
            }
        }
        addData();
        System.out.println("The Faculty info had been filled successfully.");
    }
}
