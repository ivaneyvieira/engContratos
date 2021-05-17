package br.com.astrosoft.engContratos.model.beans

import br.com.astrosoft.engContratos.model.saci
import java.time.LocalDate

class Fornecedor(
  val vendno: Int,
  val nome: String,
  val abreviacao: String,
  val custno: Int,
  val nomeFantasia: String,
  val ultimaData: LocalDate?
                ) {
  fun notaFornecedor() = saci.listNotaFornecedor(this)

  companion object {
    fun findFornecedores(filtro: FiltroFonecedor) = saci.listFornecedores(filtro)
  }
}

data class FiltroFonecedor(val localizador: String)