package com.example.pageservice;


import com.example.pageservice.model.Page;
import com.example.pageservice.repository.PageRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PageControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PageRepository pageRepository;

    private ObjectMapper mapper = new ObjectMapper();

    List<String> itemsPageOne = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
    List<String> itemsPageTwo = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"));
    List<String> itemsPageThree = new ArrayList<>(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"));
    List<String> itemsPageFour = new ArrayList<>(Arrays.asList("nijntje", "schommel"));


    private Page pageOne = new Page(1, itemsPageOne, true, "Nijntje in de speeltuin");
    private Page pageTwo = new Page(2, itemsPageTwo, false, "Nijntje in de speeltuin");
    private Page pageThree = new Page(3, itemsPageThree, false, "Nijntje in de speeltuin");
    private Page pageToBeDeleted = new Page(1, itemsPageFour, false, "Nijntje");


    @BeforeEach
    public void beforeAllTests() {
        pageRepository.deleteAll();
        pageRepository.save(pageOne);
        pageRepository.save(pageTwo);
         pageRepository.save(pageThree);
         pageRepository.save(pageToBeDeleted);
    }

    @AfterEach
    public void afterAllTests() {
        pageRepository.deleteAll();
    }

    @Test
    public void whenGetAllPages_thenReturnJsonPage() throws Exception{
        mockMvc.perform(get("/pages"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(true)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[2].pageNumber", is(3)))
                .andExpect(jsonPath("$[2].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[2].seen", is(false)))
                .andExpect(jsonPath("$[2].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[3].pageNumber", is(1)))
                .andExpect(jsonPath("$[3].items", is(Arrays.asList("nijntje", "schommel"))))
                .andExpect(jsonPath("$[3].seen", is(false)))
                .andExpect(jsonPath("$[3].bookTitle", is("Nijntje")));

    }

    @Test
    public void givenBooktitle_whenGetPagesByBooktitle_thenReturnJsonPage() throws Exception{
        mockMvc.perform(get("/pages/booktitle/{bookTitle}", "Nijntje in de speeltuin"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(true)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[2].pageNumber", is(3)))
                .andExpect(jsonPath("$[2].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[2].seen", is(false)))
                .andExpect(jsonPath("$[2].bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    public void givenBooktitleAndPageNumber_whenGetPageByBooktitleAndPageNumber_thenReturnJsonPage() throws Exception{
        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}", "Nijntje in de speeltuin", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$.seen", is(true)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    public void givenBooktitleAndPageNumber_whenGetItemsByBooktitleeAndPageNumber_thenReturnList() throws Exception{
        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}/items", "Nijntje in de speeltuin", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))));
    }

    //TODO
    @Test
    public void givenBooktitleAndPageNumber_whenGetPagesUnseenByBooktitleeAndPageNumber_thenReturnDouble() throws Exception{
        mockMvc.perform(get("/pages/booktitle/{bookTitle}/pagesunseen", "Nijntje in de speeltuin"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(0.33)));
    }




    @Test
    public void givenBooktitle_whenSetPagesUnseen_thenReturnJsonPages() throws Exception{
        mockMvc.perform(put("/pages/booktitle/{bookTitle}/setpagesunseen", "Nijntje in de speeltuin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].pageNumber", is(1)))
                .andExpect(jsonPath("$[0].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[0].seen", is(false)))
                .andExpect(jsonPath("$[0].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[1].pageNumber", is(2)))
                .andExpect(jsonPath("$[1].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje", "auto"))))
                .andExpect(jsonPath("$[1].seen", is(false)))
                .andExpect(jsonPath("$[1].bookTitle", is("Nijntje in de speeltuin")))
                .andExpect(jsonPath("$[2].pageNumber", is(3)))
                .andExpect(jsonPath("$[2].items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$[2].seen", is(false)))
                .andExpect(jsonPath("$[2].bookTitle", is("Nijntje in de speeltuin")))
        ;
    }



    @Test
    public void whenPostPage_thenReturnJsonPage() throws Exception {

        Page pageFive = new Page(5, itemsPageFour, false, "Nijntje in de speeltuin");


        mockMvc.perform(post("/pages")
                        .content(mapper.writeValueAsString(pageFive))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(5)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "schommel"))))
                .andExpect(jsonPath("$.seen", is(false)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));

    }

    @Test
    public void givenPage_whenPutPage_thenReturnJsonPage() throws Exception {
        Page updatedPageOne = new Page(1, itemsPageOne, false, "Nijntje in de speeltuin");

        mockMvc.perform(put("/pages")
                        .content(mapper.writeValueAsString(updatedPageOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pageNumber", is(1)))
                .andExpect(jsonPath("$.items", is(Arrays.asList("nijntje", "mamaNijntje", "papaNijntje"))))
                .andExpect(jsonPath("$.seen", is(false)))
                .andExpect(jsonPath("$.bookTitle", is("Nijntje in de speeltuin")));
    }

    @Test
    public void givenReview_whenDeleteReview_thenStatusOk() throws Exception {
        mockMvc.perform(delete("/pages/booktitle/{bookTitle}/pagenumber/{pageNumber}", "Nijntje", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void givenNoReview_whenDeleteReview_thenStatusNotFound() throws Exception {
        mockMvc.perform(delete("/reviews/user/{userId}/book/{ISBN}", "Nijntje", 5)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
