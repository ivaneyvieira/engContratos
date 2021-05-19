package br.com.astrosoft.engContratos.view.reports

import br.com.astrosoft.engContratos.model.beans.NotaEntrada
import br.com.astrosoft.engContratos.model.beans.ProdutosNotaSaida
import br.com.astrosoft.framework.util.format
import br.com.astrosoft.framework.view.reports.*
import br.com.astrosoft.framework.view.reports.Templates.fieldFontGrande
import net.sf.dynamicreports.jasper.builder.JasperReportBuilder
import net.sf.dynamicreports.report.builder.DynamicReports.*
import net.sf.dynamicreports.report.builder.column.ColumnBuilder
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder
import net.sf.dynamicreports.report.builder.component.ComponentBuilder
import net.sf.dynamicreports.report.builder.subtotal.SubtotalBuilder
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment.*
import net.sf.jasperreports.engine.export.JRPdfExporter
import net.sf.jasperreports.export.SimpleExporterInput
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalTime

class RelatorioNotaDevolucao(val notaSaida: NotaEntrada) {
  val codigoCol: TextColumnBuilder<String> =
    col.column("Cód Saci", ProdutosNotaSaida::codigo.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(RIGHT) //this.setPattern("000000")
      this.setFixedWidth(40)
    }
  val refForCol: TextColumnBuilder<String> =
    col.column("Ref do Fab", ProdutosNotaSaida::refFor.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(80)
    }
  val descricaoCol: TextColumnBuilder<String> = col.column(
    "Descrição", ProdutosNotaSaida::descricao.name, type.stringType()
                                                          ).apply {
    this.setHorizontalTextAlignment(LEFT) //this.setFixedWidth(60 * 4)
  }
  val gradeCol: TextColumnBuilder<String> =
    col.column("Grade", ProdutosNotaSaida::grade.name, type.stringType()).apply {
      this.setHorizontalTextAlignment(CENTER)
      this.setFixedWidth(50)
    }
  val stCol: TextColumnBuilder<String> = col.column("ST", ProdutosNotaSaida::st.name, type.stringType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setFixedWidth(25)
  }
  val qtdeCol: TextColumnBuilder<Int> = col.column("Quant", ProdutosNotaSaida::qtde.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("0")
    this.setFixedWidth(40)
  }
  val itemCol: TextColumnBuilder<Int> = col.column("Item", ProdutosNotaSaida::item.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setPattern("000")
    this.setFixedWidth(25)
  }
  val valorUnitarioCol: TextColumnBuilder<Double> = col.column(
    "V. Unit", ProdutosNotaSaida::valorUnitario.name, type.doubleType()
                                                              ).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("#,##0.00")
    this.setFixedWidth(50)
  }
  val valorTotalCol: TextColumnBuilder<Double> = col.column(
    "V. Total", ProdutosNotaSaida::valorTotal.name, type.doubleType()
                                                           ).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("#,##0.00")
    this.setFixedWidth(60)
  }
  val valorTotalIpiCol: TextColumnBuilder<Double> = col.column(
    "R$ Total Geral", ProdutosNotaSaida::valorTotalIpi.name, type.doubleType()
                                                              ).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("#,##0.00")
    this.setFixedWidth(60)
  }
  val ipiCol: TextColumnBuilder<Double> =
    col.column("Valor Ipi", ProdutosNotaSaida::ipi.name, type.doubleType()).apply {
      this.setHorizontalTextAlignment(RIGHT)
      this.setPattern("#,##0.00")
      this.setFixedWidth(50)
    }
  val vstCol: TextColumnBuilder<Double> = col.column("Valor ST", ProdutosNotaSaida::vst.name, type.doubleType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("#,##0.00")
    this.setFixedWidth(50)
  }

  val unCol: TextColumnBuilder<String> = col.column("Unid", ProdutosNotaSaida::un.name, type.stringType()).apply {
    this.setHorizontalTextAlignment(CENTER)
    this.setFixedWidth(30)
  }
  val invnoCol: TextColumnBuilder<Int> = col.column("NI", ProdutosNotaSaida::invno.name, type.integerType()).apply {
    this.setHorizontalTextAlignment(RIGHT)
    this.setPattern("0")
    this.setFixedWidth(50)
  }

  private fun columnBuilder(): List<ColumnBuilder<*, *>> {
    return listOf(
      itemCol,
      refForCol,
      codigoCol,
      descricaoCol,
      gradeCol,
      unCol,
      stCol,
      qtdeCol,
      valorUnitarioCol,
      valorTotalCol,
                 )
  }

  private fun titleBuiderNota01(): ComponentBuilder<*, *> {
    return verticalBlock {
      horizontalList {
        text("ENGECOPI ${notaSaida.sigla}", CENTER).apply {
          this.setStyle(fieldFontGrande)
        }
      }
      horizontalList {
        val dataAtual = LocalDate.now().format()
        val horaAtual = LocalTime.now().format()
        val fornecedor = notaSaida.vendno
        val vendno = notaSaida.vendno
        val nota = notaSaida.nota
        val dataNota = notaSaida.dataNota.format()
        val fatura = notaSaida.fatura
        text("$vendno - $fornecedor  NDF $nota - $dataNota   DUP $fatura", LEFT)
        text("$dataAtual-$horaAtual", RIGHT, 100)
      }
    }
  }

  private fun titleBuider(): ComponentBuilder<*, *> {
    return  titleBuiderNota01()
  }

  private fun pageFooterBuilder(): ComponentBuilder<*, *>? {
    return cmp.verticalList()
  }

  private fun subtotalBuilder(): List<SubtotalBuilder<*, *>> {
    return listOf(
      sbt.text("Total R$", valorUnitarioCol),
      sbt.sum(valorTotalCol),
      sbt.sum(ipiCol),
      sbt.sum(vstCol),
      sbt.sum(valorTotalIpiCol),
               )
  }

  private fun sumaryBuild(): ComponentBuilder<*, *> {
    return verticalBlock {
      breakLine()

      text("OBSERVAÇÕES:", LEFT, 100)
      text(notaSaida.obsNota, LEFT)
    }
  }

  fun makeReport(): JasperReportBuilder? {
    val colunms = columnBuilder().toTypedArray()
    var index = 1
    val itens = notaSaida.listaProdutos().sortedBy { it.codigo.toIntOrNull() ?: 0 }.map {
      it.apply {
        item = index++
      }
    }
    return report().title(titleBuider()).setTemplate(Templates.reportTemplate).columns(* colunms).columnGrid(* colunms)
      .setDataSource(itens).setPageMargin(margin(28)).summary(sumaryBuild())
      .summary(pageFooterBuilder()).subtotalsAtSummary(* subtotalBuilder().toTypedArray())
      .setSubtotalStyle(stl.style().setPadding(2).setTopBorder(stl.pen1Point()))
      .pageFooter(cmp.pageNumber().setHorizontalTextAlignment(RIGHT).setStyle(stl.style().setFontSize(8)))
  }

  companion object {
    fun processaRelatorio(listNota: List<NotaEntrada>): ByteArray {
      val printList = listNota.map { nota ->
        val report = RelatorioNotaDevolucao(nota).makeReport()
        report?.toJasperPrint()
      }
      val exporter = JRPdfExporter()
      val out = ByteArrayOutputStream()
      exporter.setExporterInput(SimpleExporterInput.getInstance(printList))

      exporter.exporterOutput = SimpleOutputStreamExporterOutput(out)

      exporter.exportReport()
      return out.toByteArray()
    }
  }
}