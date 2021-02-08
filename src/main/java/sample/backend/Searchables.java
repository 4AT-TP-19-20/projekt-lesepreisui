package sample.backend;

import sample.backend.data.Data;

public class Searchables {
    static public boolean contain(String input, Searchable... searchables) {
        input = input.trim();

        if(Data.settings.useAndInSearch()) {
            for(String part : input.split(" ")) {
                if(!searchTermInList(part, searchables)) {
                    return false;
                }
            }
            return true;
        }
        else {
            for(String part : input.split(" ")) {
                if(searchTermInList(part, searchables)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean searchTermInList(String term, Searchable... searchables) {
        for(Searchable searchable : searchables) {
            if(searchable.search(term)) {
                return true;
            }
        }
        return false;
    }
}
