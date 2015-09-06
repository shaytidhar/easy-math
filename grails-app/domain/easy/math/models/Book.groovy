package easy.math.models

/**
 * Created by moran on 04/06/15.
 */
class Book {

    static mapping = {
        collection "books"
    }

    static constraints = {
        created nullable: false, blank: false
        modified nullable: true, blank: true
        title nullable: false, blank: false
        publishing nullable: true, blank: true
        subject nullable: true, blank: true
    }

    String          id
    Date            created
    Date            modified
    String          title
    String          publishing
    String          subject

//    static belongsTo = [ author : Author ]
//    static hasMany = [ exercises :Exercise]
}
