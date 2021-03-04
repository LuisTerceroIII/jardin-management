package com.jardin.api.controllers.garment.especialResponses;

import com.jardin.api.models.entities.Garment;

public class CreateGarmentResponse {

  private boolean created;
  private Garment createdGarment;

  public CreateGarmentResponse() {}

  public CreateGarmentResponse(boolean created, Garment createdGarment) {
    this.created = created;
    this.createdGarment = createdGarment;
  }

  public boolean isCreated() {
    return created;
  }

  public Garment getCreatedGarment() {
    return createdGarment;
  }

  public void setCreated(boolean created) {
    this.created = created;
  }

  public void setCreatedGarment(Garment createdGarment) {
    this.createdGarment = createdGarment;
  }

  @Override
  public String toString() {
    return (
      "CreateGarmentResponse{" +
      "created=" +
      created +
      ", createdGarment=" +
      createdGarment +
      '}'
    );
  }
}
