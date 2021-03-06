/* Copyright 2018 Richard Oliver. Licensed under Apache Licence version 2.0 */
package rich

/** The top level compositional unit of Syntax in CRON: Compact Readable Object Notation. A statement can be claused consisting of comma separated clauses containing a single 
 *  expression. An empty statement is a special case of the UnClausedStatement where the semicolon character is the expression. */
sealed trait Statement extends FileSpan
{
   def optSemi: Option[SemicolonToken]
   def errGet[A](implicit ev: Persist[A]): EMon[A]   
}

object Statement
{
   implicit class StatementSeqImplicit(statementSeq: Seq[Statement]) extends FileSpan
   {
      private def ifEmptyFilePosn: FilePosn = FilePosn(0, 0 , "Empty Statement Seq")
      def startPosn = statementSeq.ifEmpty(ifEmptyFilePosn, statementSeq.head.startPosn)
      def endPosn = statementSeq.ifEmpty(ifEmptyFilePosn, statementSeq.last.endPosn)
      def errGet1[A1](implicit ev1: Persist[A1]): EMon[(A1)] = statementSeq match
      {         
         case Seq(h1) => h1.errGet[A1](ev1)
         case s => bad1(s, s.length.toString -- "statements not 1")
      }
      def errGet2[A1, A2](implicit ev1: Persist[A1], ev2: Persist[A2]): EMon[(A1, A2)] = statementSeq match
      {         
         case Seq(h1, h2) => h1.errGet[A1](ev1).map2(h2.errGet[A2](ev2), (g1, g2: A2) => (g1, g2))
         case s => bad1(s, s.length.toString -- "statements not 2")
      }
      def errGet3[A1, A2, A3](implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3]): EMon[(A1, A2, A3)] = statementSeq match
      {         
         case Seq(h1, h2, h3) => h1.errGet[A1](ev1)map3(h2.errGet[A2](ev2), h3.errGet[A3](ev3), (g1: A1, g2: A2, g3: A3) => (g1, g2, g3))
         case s => bad1(s, s.length.toString -- "statements not 3")
      }
      def errGet4[A1, A2, A3, A4](implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3], ev4: Persist[A4]):
         EMon[(A1, A2, A3, A4)] = statementSeq match
      {         
         case Seq(h1, h2, h3, h4) => h1.errGet[A1](ev1)map4(h2.errGet[A2](ev2), h3.errGet[A3](ev3), h4.errGet[A4],
               (g1: A1, g2: A2, g3: A3, g4: A4) => (g1, g2, g3, g4))
         case s => bad1(s, s.length.toString -- "statements not 4")
      } 
      
      def errFun2[A1, A2, B](f2: (A1, A2) => B)(implicit ev1: Persist[A1], ev2: Persist[A2]): EMon[B] = errGet2[A1, A2].map(f2.tupled(_))
      
      def errFun3[A1, A2, A3, B](f3: (A1, A2, A3) => B)(implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3]): EMon[B] =
         errGet3[A1, A2, A3].map(f3.tupled(_))
         
      def errFun4[A1, A2, A3, A4, B](f4: (A1, A2, A3, A4) => B)(implicit ev1: Persist[A1], ev2: Persist[A2], ev3: Persist[A3],
            ev4: Persist[A4]): EMon[B] = errGet4[A1, A2, A3, A4].map(f4.tupled(_)) 
            
      def findType[A](implicit ev: Persist[A]): EMon[A] = ev.fromStatementSeq(statementSeq)
         
   }
   implicit class EmonStatementSeqImplict(eMon: EMon[Seq[Statement]])
   {
      def errGet1[A1](implicit ev1: Persist[A1]): EMon[(A1)] = eMon.flatMap(_.errGet1[A1])
   }
}

/** This statement has 1 or more comma separated clauses. The first clause must be terminated by a comma. Subsequent clauses have an optional
 *  closing comma. */
case class ClausedStatement(clauses: Seq[Clause], optSemi: Option[SemicolonToken]) extends Statement with FileSpanMems
{
   def startMem: FileSpan = clauses.head
   def endMem: FileSpan = optSemi.fold[FileSpan](clauses.last)(st => st)
   override def errGet[A](implicit ev: Persist[A]): EMon[A] = ev.fromClauses(clauses)
}

/** An unclaused Statement has a single expression. */

sealed trait UnClausedStatement extends Statement
{
   def expr: Expr
   def optSemi: Option[SemicolonToken]
   override def errGet[A](implicit ev: Persist[A]): EMon[A] = ev.fromExpr(expr)
}
case class MonoStatement(expr: Expr, optSemi: Option[SemicolonToken]) extends UnClausedStatement with FileSpanMems
{
   def startMem: FileSpan = expr
   def endMem: FileSpan = optSemi.fold(expr)(sc => sc)   
}

/* The Semicolon of the Empty statement is the expression of this special case of the unclaused statement */
case class EmptyStatement(st: SemicolonToken) extends UnClausedStatement with FileSpanMems
{
   override def expr = st
   override def optSemi: Option[SemicolonToken] = Some(st)
   override def startMem: FileSpan = st
   override def endMem: FileSpan = st
   def asError[A]: Bad[A] = bad1(st.startPosn, "Empty Statement")
}
object EmptyStatement
{
   def apply(st: SemicolonToken): EmptyStatement = new EmptyStatement(st)
   
}
