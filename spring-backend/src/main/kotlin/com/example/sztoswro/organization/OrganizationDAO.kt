package com.example.sztoswro.organization

import com.example.sztoswro.member.MemberDAO
import com.example.sztoswro.member.RoleLevel
import javax.persistence.*

@Entity
@Table(name = "organization")
data class OrganizationDAO (

        @Id
        var id: Long,
        @Column(unique = true)
        var name: String,
        var email: String,
        var about: String,
        var type: String,
        var careProvider: String,
        val registerFaculty: String,
        val activitiesArea: String,
        val facultiesArea: String,
        @ElementCollection
    var organizationRoles: Map<String, String>,
        @ManyToMany
        var members: MutableSet<MemberDAO>
) {
    constructor(org: OrganizationDTO) : this(
        org.id,
        org.name,
        org.email,
        org.about,
        org.type,
        org.careProvider,
        org.registerFaculty,
        org.activitiesArea,
        org.facultiesArea,
            emptyMap(),
            mutableSetOf()
    )

    constructor() : this(1, "hej", "ho", "hej", "ho", "hej", "sokoly", "omijajcie", "gory lasy doły", emptyMap(), mutableSetOf())

    fun update(updateData: OrganizationDAO){
        this.name = updateData.name;
        this.email = updateData.email;
        this.about = updateData.about;
        this.type = updateData.type;
        this.careProvider = updateData.careProvider;
        this.organizationRoles = updateData.organizationRoles;
        this.members = updateData.members
    }

}