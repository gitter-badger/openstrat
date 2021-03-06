/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */package rich
package pParse

//import rich.HInput
//import rich.HNotVoid
//import rich.HtmlEl
//import rich.XAtt
//import rich.stringToRichImp

class HForm(memsIn: Seq[HtmlEl], attsIn: Seq[XAtt]) extends HNotVoid
{
   def tag = "form"
   override val mems = memsIn 
   override val atts = attsIn :+ FormPost
}
case class FormSubmit(attsIn: Seq[XAtt]) extends HInput { override def atts: Seq[XAtt] = XAtt("type", "submit") +: attsIn }
case class FormOne(infoStr: String, label: String) extends HForm(Seq(InputSubmit(label, Seq(FormName(infoStr)))), Nil)
case class InputSubmit(str: String, otherAttribs: Seq[XAtt] = Seq()) extends HInput
{   
   override def atts: Seq[XAtt] = Seq[XAtt](XAtt("type", "submit"), StyleAtt(CssFontCent(300)), FormValue(str)) ++ otherAttribs
}

object FormPost extends XAtt("method","post")
case class FormValue(label: String) extends XAtt("value", label.enqu)
case class FormAction(actionName: String) extends XAtt("action", actionName.enqu)
case class FormName(nameStr: String) extends XAtt("name", nameStr.enqu)