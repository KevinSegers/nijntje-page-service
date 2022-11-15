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

            final String title = "Nijntje in de speeltuin";

            pageRepository.save(new Page(1, itemsPageOne, false, title));
            pageRepository.save(new Page(2, itemsPageTwo, false, title));
            pageRepository.save(new Page(3, itemsPageThree, false, title));
            pageRepository.save(new Page(4, itemsPageFour, false, title));
            pageRepository.save(new Page(5, itemsPageFive, false, title));
            pageRepository.save(new Page(6, itemsPageSix, false, title));
            pageRepository.save(new Page(7, itemsPageSeven, false, title));
            pageRepository.save(new Page(8, itemsPageEight, false, title));
            pageRepository.save(new Page(9, itemsPageNine, false, title));
            pageRepository.save(new Page(10, itemsPageTen, false, title));
            pageRepository.save(new Page(11, itemsPageEleven, false, title));
            pageRepository.save(new Page(12, itemsPageTwelve, false, title));

        }
    }

    // Get all pages from book
    @GetMapping("/pages/booktitle/{bookTitle}")
    public List<Page> getPagesByBookTitle(@PathVariable String bookTitle){
        return pageRepository.findPagesByBookTitle(bookTitle);
    }


    // Get one page by booktitle and pagenumber
    @GetMapping("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}")
    public Page getPageByBookTitleAndPageNumber(@PathVariable String bookTitle, @PathVariable int pageNumber){

        return pageRepository.findPageByBookTitleAndPageNumber(bookTitle, pageNumber);

    }

    // Get all pages
    @GetMapping("/pages")
    public List<Page> getAllPages(){
        return pageRepository.findAll();
    }

    // Get seen pages in double
    @GetMapping("/pages/booktitle/{bookTitle}/pagesseen")
    public double getPagesUnseenFromBook(@PathVariable String bookTitle){
        List<Page> pages = pageRepository.findPagesByBookTitle(bookTitle);
        assert pages != null;
        int totalSeen = 0;
        int totalPages = pages.size();
        for (Page page:pages) {
            if(page.isSeen()) totalSeen++;
        }

        return (double) Math.round(100*(double) totalSeen/totalPages)/100;

    }

    // Get all items from a page
    @GetMapping("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}/items")
    public List<String> getItemsFromPage(@PathVariable String bookTitle, @PathVariable int pageNumber){
        return pageRepository.findPageByBookTitleAndPageNumber(bookTitle, pageNumber).getItems();
    }


    // Post a page
    @PostMapping("/pages")
    public Page addPage(@RequestBody Page page){

        pageRepository.save(page);

        return page;
    }

    // Update a page
    @PutMapping("/pages")
    public Page updatePage(@RequestBody Page updatedPage){
        Page retrievedPage = pageRepository.findPageByBookTitleAndPageNumber(updatedPage.getBookTitle(), updatedPage.getPageNumber());

        retrievedPage.setPageNumber(updatedPage.getPageNumber());
        retrievedPage.setItems(updatedPage.getItems());
        retrievedPage.setSeen(updatedPage.isSeen());
        retrievedPage.setBookTitle(updatedPage.getBookTitle());


        pageRepository.save(retrievedPage);

        return retrievedPage;
    }

    // Set all pages of a book unseen
    @PutMapping("/pages/booktitle/{bookTitle}/setpagesunseen")
    public List<Page> setPagesUnseen(@PathVariable String bookTitle){
        List<Page> pages = pageRepository.findPagesByBookTitle(bookTitle);
        for(Page page : pages){
            page.setSeen(false);
            pageRepository.save(page);
        }
        return pages;
    }

    // delete a page
    @DeleteMapping("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}")
    public ResponseEntity<Page> deletePage(@PathVariable String bookTitle, @PathVariable int pageNumber) {
        Page page = pageRepository.findPageByBookTitleAndPageNumber(bookTitle, pageNumber);
        if (page != null) {
            pageRepository.delete(page);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
