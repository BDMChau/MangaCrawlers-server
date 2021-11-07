package serverapi.query.specification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import serverapi.query.dtos.features.SearchCriteriaDTO;
import serverapi.tables.forum.post.Post;
import serverapi.tables.manga_tables.manga.Manga;
import serverapi.tables.user_tables.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Locale;

public class Specificationn {
    private final SearchCriteriaDTO searchCriteriaDTO;

    @Autowired
    public Specificationn(SearchCriteriaDTO searchCriteriaDTO) {
        this.searchCriteriaDTO = searchCriteriaDTO;
    }


    public class SearchingManga implements Specification<Manga> {
        @Override
        public Predicate toPredicate(Root<Manga> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            if (searchCriteriaDTO.getOperation().equalsIgnoreCase(">")) {

                return builder.greaterThanOrEqualTo(
                        root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(builder.lower(root.get(searchCriteriaDTO.getKey())),
                        searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase(":")) {

                if (root.get(searchCriteriaDTO.getKey()).getJavaType() == String.class) {// search page
                    return builder.like(
                            builder.lower(root.get(searchCriteriaDTO.getKey())),
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
                        root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(builder.lower(root.get(searchCriteriaDTO.getKey())),
                        searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase(":")) {

                if (root.get(searchCriteriaDTO.getKey()).getJavaType() == String.class) {// search page
                    return builder.like(
                            builder.lower(root.get(searchCriteriaDTO.getKey())),
                            "%" + searchCriteriaDTO.getValue().toLowerCase(Locale.ROOT) + "%");
                } else {
                    return builder.equal(root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
                }
            }
            return null;
        }
    }


    public class SearchingPosts implements Specification<Post> {
        @Override
        public Predicate toPredicate(Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            if (searchCriteriaDTO.getOperation().equalsIgnoreCase(">")) {

                return builder.greaterThanOrEqualTo(
                        root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase("<")) {
                return builder.lessThanOrEqualTo(builder.lower(root.get(searchCriteriaDTO.getKey())),
                        searchCriteriaDTO.getValue());
            } else if (searchCriteriaDTO.getOperation().equalsIgnoreCase(":")) {

                if (root.get(searchCriteriaDTO.getKey()).getJavaType() == String.class) {// search page
                    return builder.like(
                            builder.lower(root.get(searchCriteriaDTO.getKey())),
                            "%" + searchCriteriaDTO.getValue().toLowerCase(Locale.ROOT) + "%");
                } else {
                    return builder.equal(root.get(searchCriteriaDTO.getKey()), searchCriteriaDTO.getValue());
                }
            }
            return null;
        }
    }
}
