package easy.math.models

/**
 * Created by moran on 04/06/15.
 */
class Solution {

    static mapping = {
        collection "solutions"
    }

    static constraints = {
        created nullable: false, blank: false
        modified nullable: true, blank: true
        solution nullable: false, blank: false
        bookTitle nullable: true, blank: true
        authorName nullable: true, blank: true
        pageNumber nullable: true, blank: true
        exerciseNumber nullable: true, blank: true
    }

    String          id
    Date            created
    Date            modified
    String          solution
    String          bookTitle
    String          authorName
    Integer         pageNumber
    Integer         exerciseNumber
}
