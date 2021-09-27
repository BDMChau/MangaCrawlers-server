package serverapi.query.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import serverapi.query.dtos.features.SearchCriteriaDTO;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

public class Specificationn {
    private SearchCriteriaDTO searchCriteriaDTO;

    @Autowired
    public Specificationn(SearchCriteriaDTO searchCriteriaDTO) {
        this.searchCriteriaDTO = searchCriteriaDTO;
    }




    public class SearchingManga implements Specification<Manga> {
        @Override
        public Predicate toPredicate(Root<Manga> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            if (searchCriteriaDTO.getOperation().equalsIgnoreCase(">")) {

                return builder.greaterThanOrEqualTo(
                        root.<String>get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue().toString());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(builder.lower(root.<String>get(searchCriteriaDTO.getKey())),
                        searchCriteriaDTO.getValue().toString());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase(":")) {

                if (root.get(searchCriteriaDTO.getKey()).getJavaType() == String.class) {// search page
                    return builder.like(
                            builder.lower(root.<String>get(searchCriteriaDTO.getKey())),
                            "%" + searchCriteriaDTO.getValue().toLowerCase(Locale.ROOT) + "%");
                } else {
                    return builder.equal(root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
                }
            }
            return null;
        }
    }


    public class SearchingUsers implements Specification<User> {
        @Override
        public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            if (searchCriteriaDTO.getOperation().equalsIgnoreCase(">")) {

                return builder.greaterThanOrEqualTo(
                        root.<String>get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue().toString());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(builder.lower(root.<String>get(searchCriteriaDTO.getKey())),
                        searchCriteriaDTO.getValue().toString());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase(":")) {

                if (root.get(searchCriteriaDTO.getKey()).getJavaType() == String.class) {// search page
                    return builder.like(
                            builder.lower(root.<String>get(searchCriteriaDTO.getKey())),
                            "%" + searchCriteriaDTO.getValue().toLowerCase(Locale.ROOT) + "%");
                } else {
                    return builder.equal(root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
                }
            }
            return null;
        }
    }
}
