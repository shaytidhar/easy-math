package easy.math.models

/**
 * Created by moran on 04/06/15.
 */
class Author {

    static mapping = {
        collection "authors"
    }

    static constraints = {
        created nullable: false, blank: false
        modified nullable: true, blank: true
        firstName nullable: false, blank: false
        lastName nullable: false, blank: false
    }

    String  id
    Date    created
    Date    modified
    String  firstName
    String  lastName

    static hasMany = [ books : Book]
}
