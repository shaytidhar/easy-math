package easy.math.models

/**
 * Created by moran on 04/06/15.
 */
class Exercise {

    static mapping = {
        collection "exercises"
    }

    static constraints = {
        created nullable: false, blank: false
        modified nullable: true, blank: true
        exercise nullable: false, blank: false
        solution nullable: true, blank: true
    }

    String  id
    Date    created
    Date    modified
    String  exercise
    String  solution

    static belongsTo = [ book : Book ]
}
