package com.dar.backend.api;

public enum Category {
    MUSIC("music", "Concerts &amp;amp; Tour Dates"),
    CONFERENCE("conference", "Conferences &amp;amp; Tradeshows"),
    COMEDY("comedy", "Comedy"),
    EDUCATION("learning_education", "Education"),
    FAMILY("family_fun_kids", "Kids &amp;amp; Family"),
    FESTIVALS("festivals_parades", "Festivals"),
    MOVIES("movies_film", "Film"),
    FOOD("food", "Food &amp;amp; Wine"),
    FUNDRAISERS("fundraisers", "Fundraising &amp;amp; Charity"),
    ART("art", "Art Galleries &amp;amp; Exhibits"),
    HEALTH("support", "Health &amp;amp; Wellness"),
    HOLIDAY("holiday", "Holiday"),
    BOOKS("books", "Literary &amp;amp; Books"),
    MUSEUMS("attractions", "Museums &amp;amp; Attractions"),
    BUSINESS("business", "Business &amp;amp; Networking"),
    NIGHTLIFE("singles_social", "Nightlife &amp;amp; Singles"),
    UNIVERSITY("schools_alumni", "University &amp;amp; Alumni"),
    ASSOCIATIONS("clubs_associations", "Organizations &amp;amp; Meetups"),
    OUTDOORS("outdoors_recreation", "Outdoors &amp;amp; Recreation"),
    PERFORMING_ARTS("performing_arts", "Performing Arts"),
    ANIMALS("animals", "Pets"),
    POLITIC("politic_activism", "Politics &amp;amp; Activism"),
    SALES("sales", "Sales &amp;amp; Retail"),
    SCIENCE("science", "Science"),
    RELIGION("religion_spirituality", "Religion &amp;amp; Spirituality"),
    SPORTS("sports", "Sports"),
    TECHNOLOGY("technology", "Technology"),
    OTHER("other", "Other &amp;amp; Miscellaneous"),
    ;

    private final String id;
    private final String name;

    private Category(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
}