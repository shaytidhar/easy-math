package easy.math

import grails.transaction.Transactional

import javax.annotation.PostConstruct

@Transactional
class EntitiesService {

    @PostConstruct
    def init(){

    }

    def List<Object> listEntities(
            Class entityDomain,
            String filter,
            String limit,
            String offset,
            String sort) {

        List<Object> listOfEntities = []

        // Set query by params
        def orderNodes = sort ? createOrderNodes(sort) : []
        def queryNodes = createQueryNodes(filter)
        def paginationNodes = (offset && limit) ? createPaginationNodes(offset, limit) : []

        // Get entities
        def entitiesCriteria = entityDomain.createCriteria()
        listOfEntities = entitiesCriteria {

            queryNodes.each { it.delegate = delegate }
            paginationNodes.each { it.delegate = delegate }
            orderNodes.each { it.delegate = delegate }

            // Filter
            if(!queryNodes.isEmpty()) {
                or {
                    queryNodes.each { it() }
                }
            }
            // Pagination
            paginationNodes.each { it() }
            // Sort
            orderNodes.each { it() }
        }

        return (listOfEntities)
    }

    protected List<Closure> createPaginationNodes(String offset, String limit) {

        def paginationNodes = []

        // Not Null and numbers
        if (limit?.isNumber() && offset?.isNumber()) {

            int limitAsInt = Integer.parseInt(limit)
            int offsetAsInt = Integer.parseInt(offset)

            paginationNodes =
                    [ { maxResults limitAsInt },
                      { firstResult offsetAsInt } ]
        }

        return (paginationNodes)
    }

    protected List<Closure> createQueryNodes(String filter) {

        def queryNodes = []

        // Not null
        if (filter) {

            // For each field
            filter.split("\\|").each{

                def (filterField, filterValue) = it.split("::")

                queryNodes += { -> like(filterField, "%" + filterValue + "%") }
            }
        }

        return (queryNodes)
    }

    protected List<Closure> createOrderNodes(String sort) {

        def orderNodes = []

        // Not null
        if (sort) {

            // For each field
            sort.split("\\|").each {

                def (field, sortOrder) = (it[0] == "-") ? ([it.getAt(1..(it.length() - 1)), "desc"]) : ([it, "asc"])
                orderNodes += { -> order(field, sortOrder) }
            }
        }

        return (orderNodes)
    }
}
