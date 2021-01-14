package com.jardin.api.repositories;

import com.jardin.api.model.entities.Garment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;



public interface GarmentRepository extends JpaRepository<Garment,Long> {

    //SELECTS
    @Query(nativeQuery = true,value = "SELECT * from Garment g order by g.id LIMIT :limit OFFSET :offset")
    List<Garment> getGarmentWithPagination(@Param("limit") Integer limit, @Param("offset") Integer offset);

    // UPDATES
    @Modifying
    @Query("UPDATE Garment g set g.type = :type, g.size = :size, g.mainColor = :mainColor, g.gender = :gender, g.mainMaterial = :mainMaterial, g.madeIn = :madeIn, g.comment = :comment, g.price = :price where g.id = :id")
    @Transactional
    void update(@Param("id") Long id,
                   @Param("type") String type,
                   @Param("size") String size,
                   @Param("mainColor") String mainColor,
                   @Param("gender") String gender,
                   @Param("mainMaterial") String mainMaterial,
                   @Param("madeIn") String madeIn,
                   @Param("comment") String comment,
                   @Param("price") Integer price);
                    // CAMBIAR TODOS LOS ATRIBUTOS DE PRODUCTO, SALVO SU ID.

}
