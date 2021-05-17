package br.com.astrosoft.engContratos.model

import br.com.astrosoft.AppConfig
import br.com.astrosoft.engContratos.model.beans.*
import br.com.astrosoft.framework.model.QueryDB
import br.com.astrosoft.framework.util.DB

class QuerySaci : QueryDB(driver, url, username, password) {
  fun findUser(login: String?): UserSaci? {
    login ?: return null
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", login)
      addParameter("appName", AppConfig.appName)
    }.firstOrNull()
  }

  fun findAllUser(): List<UserSaci> {
    val sql = "/sqlSaci/userSenha.sql"
    return query(sql, UserSaci::class) {
      addParameter("login", "TODOS")
      addParameter("appName", AppConfig.appName)
    }
  }

  fun updateUser(user: UserSaci) {
    val sql = "/sqlSaci/updateUser.sql"
    script(sql) {
      addOptionalParameter("login", user.login)
      addOptionalParameter("bitAcesso", user.bitAcesso)
      addOptionalParameter("loja", user.storeno)
      addOptionalParameter("appName", AppConfig.appName)
    }
  }

  fun allLojas(): List<Loja> {
    val sql = "/sqlSaci/listLojas.sql"
    return query(sql, Loja::class)
  }

  fun listFornecedores(filtro: FiltroFonecedor): List<Fornecedor> {
    val sql = "/sqlSaci/listFornecedores.sql"
    return query(sql, Fornecedor::class){
      addOptionalParameter("localizador", filtro.localizador)
    }
  }

  fun listNotaFornecedor(fornecedor: Fornecedor): List<NotaSaida> {
    val sql = "/sqlSaci/listNotaFornecedor.sql"
    return query(sql, NotaSaida::class){
      addOptionalParameter("vendno", fornecedor.vendno)
    }
  }

  companion object {
    private val db = DB("saci")
    internal val driver = db.driver
    internal val url = db.url
    internal val username = db.username
    internal val password = db.password
    val ipServer: String? = url.split("/").getOrNull(2)
  }
}

val saci = QuerySaci()

data class Max(val max: Int)