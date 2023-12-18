package com.kingtest.dto.responses;

import com.kingtest.services.UserDataService.UserData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class RecepcionistaSafeDto extends UserData{
    private long id;    
    // private String firstName;
    // private String lastName;
    private String fullName;
    private String email;
    private String birthday;
    private long clinicId;    

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            // ", firstName='" + getFirstName() + "'" +
            // ", lastName='" + getLastName() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", birthday='" + getBirthday() + "'" +
            ", email='" + getEmail() + "'" +
            ", clinicId='" + getClinicId() + "'" +
            ", userId='" + getUserId() + "'" +
            "}";
    }
}
