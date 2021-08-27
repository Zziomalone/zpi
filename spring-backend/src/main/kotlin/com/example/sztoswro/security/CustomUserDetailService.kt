package com.example.sztoswro.security

import com.example.sztoswro.member.MemberService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


class CustomUserDetails (
        private val aUsername:String,
        private val aPassword:String, ) : UserDetails {


    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = aUsername

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        TODO("Not yet implemented")
    }

    override fun getPassword(): String = aPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}


@Service
class CustomUserDetailsService(
        val users: MemberService
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {

        username?.let {
            val userDAO = users.findMember(it)
            return CustomUserDetails(userDAO.username, userDAO.password)
        }
        throw UsernameNotFoundException(username)
    }
}