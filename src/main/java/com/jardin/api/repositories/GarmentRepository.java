package com.jardin.api.repositories;

import com.jardin.api.model.entities.Garment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface GarmentRepository extends JpaRepository<Garment, Long> {
  //SELECTS
  @Query(
    nativeQuery = true,
    value = "SELECT * from Garment g order by g.id LIMIT :limit OFFSET :offset"
  )
  List<Garment> getGarmentWithPagination(
    @Param("limit") Integer limit,
    @Param("offset") Integer offset
  );

  // UPDATES
  @Modifying
  @Query(
    "UPDATE Garment g set g.type = :type, g.size = :size, g.mainColor = :mainColor, g.gender = :gender, g.mainMaterial = :mainMaterial, g.madeIn = :madeIn, g.comment = :comment, g.price = :price where g.id = :id"
  )
  @Transactional
  void update(
    @Param("id") Long id,
    @Param("type") String type,
    @Param("size") String size,
    @Param("mainColor") String mainColor,
    @Param("gender") String gender,
    @Param("mainMaterial") String mainMaterial,
    @Param("madeIn") String madeIn,
    @Param("comment") String comment,
    @Param("price") Integer price
  );
  // CAMBIAR TODOS LOS ATRIBUTOS DE PRODUCTO, SALVO SU ID.


  @Query(nativeQuery = true,
          value = "select * from Garment g" +
                  " where" +
                  " (g.type = :type or :type = '') and" +
                  " (g.gender = :gender or :gender = '') and" +
                  " (g.size = :size or :size = '') and" +
                  " (g.made_in = :madeIn or :madeIn  = '') and" +
                  " (g.main_material = :mainMaterial or :mainMaterial  = '') and" +
                  " (g.price >= :priceFrom or :priceFrom = 0) and" +
                  "(g.price <= :priceTo or :priceTo  = 0) " +
                  "order by g.id LIMIT :limit OFFSET :offset"
  )
  List<Garment> searchResolver(@Param("type") String type,
                               @Param("gender") String gender,
                               @Param("size") String size,
                               @Param("madeIn") String madeIn,
                               @Param("mainMaterial") String mainMaterial,
                               @Param("priceFrom") Integer priceFrom,
                               @Param("priceTo") Integer priceTo,
                               @Param("limit") Integer limit,
                               @Param("offset") Integer offset);


  @Query(nativeQuery = true,
          value = "select count(*) from Garment g" +
                  " where" +
                  " (g.type = :type or :type = '') and" +
                  " (g.gender = :gender or :gender = '') and" +
                  " (g.size = :size or :size = '') and" +
                  " (g.made_in = :madeIn or :madeIn  = '') and" +
                  " (g.main_material = :mainMaterial or :mainMaterial  = '') and" +
                  " (g.price >= :priceFrom or :priceFrom = 0) and" +
                  "(g.price <= :priceTo or :priceTo  = 0) "
  )
  Long searchResolverNoPagination(@Param("type") String type,
                               @Param("gender") String gender,
                               @Param("size") String size,
                               @Param("madeIn") String madeIn,
                               @Param("mainMaterial") String mainMaterial,
                               @Param("priceFrom") Integer priceFrom,
                               @Param("priceTo") Integer priceTo);

}
