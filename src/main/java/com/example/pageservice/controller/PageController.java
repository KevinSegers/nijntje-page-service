package com.example.pageservice.controller;

import com.example.pageservice.model.Page;

import com.example.pageservice.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class PageController {

    @Autowired
    private PageRepository pageRepository;

    @PostConstruct
    public void fillDB(){
        if(pageRepository.count() == 0){

            List<String> itemsPageOne = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
            List<String> itemsPageTwo = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"));
            List<String> itemsPageThree = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
            List<String> itemsPageFour = new ArrayList<>(Arrays.asList("nijntje", "schommel"));
            List<String> itemsPageFive = new ArrayList<>(Arrays.asList("nijntje", "ringen"));
            List<String> itemsPageSix = new ArrayList<>(Arrays.asList("nijntje", "rekstok"));
            List<String> itemsPageSeven = new ArrayList<>(Arrays.asList("nijntje", "klimboom"));
            List<String> itemsPageEight = new ArrayList<>(Arrays.asList("nijntje", "glijbaan"));
            List<String> itemsPageNine = new ArrayList<>(Arrays.asList("nijntje", "wip", "papaNijntje"));
            List<String> itemsPageTen = new ArrayList<>(Arrays.asList("nijntje", "trampoline"));
            List<String> itemsPageEleven = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
            List<String> itemsPageTwelve = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"));


            pageRepository.save(new Page(1, itemsPageOne, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(2, itemsPageTwo, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(3, itemsPageThree, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(4, itemsPageFour, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(5, itemsPageFive, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(6, itemsPageSix, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(7, itemsPageSeven, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(8, itemsPageEight, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(9, itemsPageNine, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(10, itemsPageTen, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(11, itemsPageEleven, false, "Nijntje in de speeltuin"));
            pageRepository.save(new Page(12, itemsPageTwelve, false, "Nijntje in de speeltuin"));

        }
    }

    @GetMapping("/pages/booktitle/{bookTitle}")
    public List<Page> getPagesByBookTitle(@PathVariable String bookTitle){
        return pageRepository.findPagesByBookTitle(bookTitle);
    }


    @GetMapping("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}")
    public Page getPageByBookTitleAndPageNumber(@PathVariable String bookTitle, @PathVariable int pageNumber){

        return pageRepository.findPageByBookTitleAndPageNumber(bookTitle, pageNumber);

    }

    @GetMapping("/pages")
    public List<Page> getAllPages(){
        return pageRepository.findAll();
    }

    @GetMapping("/pages/booktitle/{bookTitle}/pagesunseen")
    public double getPagesUnseenFromBook(@PathVariable String bookTitle){
        List<Page> pages = pageRepository.findPagesByBookTitle(bookTitle);
        assert pages != null;
        int totalSeen = 0;
        int totalPages = pages.size();
        for (Page page:pages) {
            if(page.isSeen()){
                totalSeen ++;
            }
        }

        double result = (double) Math.round(100*(double) totalSeen/totalPages)/100;
        return result;

    }

    @GetMapping("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}/items")
    public List<String> getItemsFromPage(@PathVariable String bookTitle, @PathVariable int pageNumber){
        return pageRepository.findPageByBookTitleAndPageNumber(bookTitle, pageNumber).getItems();
    }


    @PostMapping("/pages")
    public Page addPage(@RequestBody Page page){

        pageRepository.save(page);

        return page;
    }

    @PutMapping("/pages")
    public Page updatePage(@RequestBody Page updatedPage){
        Page retrievedPage = pageRepository.findPageById(updatedPage.getId());

        retrievedPage.setPageNumber(updatedPage.getPageNumber());
        retrievedPage.setItems(updatedPage.getItems());
        retrievedPage.setSeen(updatedPage.isSeen());
        retrievedPage.setBookTitle(updatedPage.getBookTitle());


        pageRepository.save(retrievedPage);

        return retrievedPage;
    }

    @PutMapping("/pages/booktitle/{bookTitle}/setpagesunseen")
    public List<Page> setPagesUnseen(@PathVariable String bookTitle){
        List<Page> pages = pageRepository.findPagesByBookTitle(bookTitle);
        for(Page page : pages){
            page.setSeen(false);
            pageRepository.save(page);
        }
        return pages;
    }

    @DeleteMapping("/book/page/{pageId}")
    public ResponseEntity deletePage(@PathVariable String pageId) {
        Page page = pageRepository.findPageById(pageId);
        if (page != null) {
            pageRepository.delete(page);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
