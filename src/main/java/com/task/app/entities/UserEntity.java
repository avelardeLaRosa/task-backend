package com.task.app.entities;

import com.task.app.dto.UserDTO;
import com.task.app.util.entities.AuditoryEntity;
import com.task.app.util.interfaces.IDTOManagment;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "t_user")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends AuditoryEntity implements IDTOManagment<UserDTO> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "tx_nombre")
    private String nombre;
    @Column(name = "tx_apellido")
    private String apellido;
    @Column(name = "tx_cargo")
    private String cargo;
    @Column(name = "tx_dni")
    private String dni;
    @Column(name = "tx_telefono")
    private String telefono;
    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<TaskEntity> taskEntities = new ArrayList<>();

    @Override
    public UserDTO getDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(getUniqueIdentifier());
        userDTO.setStatus(getStatus());
        userDTO.setCreateUser(getCreateUser());
        userDTO.setUpdateUser(getUpdateUser());
        userDTO.setDeleteUser(getDeleteUser());
        userDTO.setCreateDate(getCreateDate());
        userDTO.setUpdateDate(getUpdateDate());
        userDTO.setDeleteDate(getDeleteDate());

        userDTO.setNombre(this.nombre);
        userDTO.setApellido(this.apellido);
        userDTO.setCargo(this.cargo);
        userDTO.setDni(this.dni);
        userDTO.setTelefono(this.telefono);
        return userDTO;
    }

    @Override
    public void setEntity(UserDTO userDTO) {
        setStatus(userDTO.getStatus());
        setUniqueIdentifier(userDTO.getId());
        setCreateUser(userDTO.getCreateUser());
        setUpdateUser(userDTO.getUpdateUser());
        setDeleteUser(userDTO.getDeleteUser());
        setCreateDate(userDTO.getCreateDate());
        setUpdateDate(userDTO.getUpdateDate());
        setDeleteDate(userDTO.getDeleteDate());

        this.nombre = userDTO.getNombre();
        this.apellido = userDTO.getApellido();
        this.cargo = userDTO.getCargo();
        this.dni = userDTO.getDni();
        this.telefono = userDTO.getTelefono();

    }
}
