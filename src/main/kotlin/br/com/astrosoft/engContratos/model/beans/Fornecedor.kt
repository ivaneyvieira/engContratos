package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci
import java.time.LocalDate

class Fornecedor(
  val vendno: Int,
  val nome: String,
  val abreviacao: String,
  val email: String,
  val custno: Int,
  val nomeFantasia: String,
  var rmkVend: String,
  val ultimaData: LocalDate?
                ) {
  fun notaFornecedor() = saci.listNotaFornecedor(this)

  fun listEmail(): List<String> = listOf(email).filter { it != "" }

  fun saveRmkVend() {
    saci.saveRmkVend(this)
  }

  companion object {
    fun findFornecedores(filtro: FiltroFonecedor) = saci.listFornecedores(filtro)
  }
}

data class FiltroFonecedor(val localizador: String)