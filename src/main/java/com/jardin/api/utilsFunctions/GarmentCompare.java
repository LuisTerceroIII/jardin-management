package com.jardin.api.utilsFunctions;

public class GarmentCompare {

    /*
        Funciones hechas para utilizarlas como simplificaciones en stream().filter()


        Parametro 1 = propiedad de objeto de base de datos.
        Parametro 2 = propiedad de objeto query.


    */

    // Recibe dos tipos de ropa, si son iguales o el tipo que corresponde al pedido por el usuario es nullo,
    // es decir no especifico tipo (por ahora es asi), se retorna true.
    // sino falso.
    //EJ:
    // List<Garment> sameType = listaDeGarments.stream()
    //                          .filter(garment -> compareByTypeOfGarment(garment.getType(),queryGarment.getType()).collect(Collectors.toList());
    public static boolean compareByTypeOfGarment(String type, String queryType) {
        if(queryType == null) return true;
        return type.equals(queryType);
    }

    public static boolean compareBySizeOfGarment(String size, String querySize) {
        if(querySize == null) return true;
        return size.equals(querySize);
    }

    public static boolean compareByMainColorOfGarment(String mainColor, String queryMainColor) {
        if(queryMainColor == null) return true;
        return mainColor.equals(queryMainColor);
    }


    public static boolean compareByGenderOfGarment(String gender, String queryGender) {
        if(queryGender == null) return true;
        return gender.equals(queryGender);
    }

    //String mainMaterial
    public static boolean compareByMainMaterialOfGarment(String mainMaterial, String queryMainMaterial) {
        if(queryMainMaterial == null )return true;
        return mainMaterial.equals(queryMainMaterial);
    }

    //String madeIn
    public static boolean compareByMadeInOfGarment(String madeIn, String queryMadeIn) {
        if(queryMadeIn == null) return true;
        return madeIn.equals(queryMadeIn);
    }

    //Integer price
    public static boolean compareByPriceOfGarment(Integer price, Integer queryPrice) {
        if(queryPrice == null) return true;
        return price.equals(queryPrice);
    }

    //String comment
    public static boolean compareByCommentGarment(String comment, String queryComment) {
        if(queryComment == null) return false;
        return queryComment.equals(comment);
    }



}
