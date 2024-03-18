     F**********************************************************************
     F*                                                                    *
     F* PROGRAM ID  : CUS300                                               *
     F* PROGRAM NAME: SAMPLE PROGRAM                                       *
     F*                                                                    *
     F**********************************************************************
     D DSP             S             50    INZ('CUSTOMER')
     D TOTAL           S              9P 2 INZ(0)
     D NUM             S              9P 2 INZ(1)
     D CNT             S              9P 2 INZ(0)
     FCUSTOMER  UF   E           K Disk
     FORDERS    IF   E           K Disk
     FORDSUM    IF   E           K Disk
     C     *INZSR        BEGSR
     C                   EVAL      DSP='CUSTOMER REPORT'
     C                   DSPLY     DSP
     C                   ENDSR
      /free
            CNT = 0;
            TOTAL = 0;
            EXSR clrsum;
            DSPLY '------  Forward  ------';
            Setll *Loval CUSTOMER;
            Dou NOT %EOF(CUSTOMER);
                Read CUSTOMER;
                If NOT %EOF(CUSTOMER);
                   EXSR calctotal;
                   EXSR dspcus;
                   If TOTAL > 0;
                       OSCUID = CUID;
                       TOTAL *=  (TOTAL / CNT +1) * 0.1;
                       OSTOT = TOTAL;
                       OSCUNM = CUSTNM;
                       Write  ORDSUM;
                    EndIf;
                EndIf;
            EndDO;

            Begsr  calctotal;
                CNT = 0;
                TOTAL = 0;
                Setll *Loval ORDERS;
                Dou NOT %EOF(ORDERS);
                    Read ORDERS;
                    If NOT %EOF(ORDERS);
                        If CUID = ORCUID;
                            TOTAL += ORTOT;
                            CNT += 1;
                            Update ORDERS;
                        EndIf;
                    EndIf;
                EndDo;
            EndSr;

            Begsr  dspcus;
                If TOTAL > 0;
                    eval DSP='CUSTOMER: ' + CUSTNM + ' $' + TOTAL;
                    DSPLY     DSP;
                EndIf;
            EndSr;

            Begsr  clrsum;
                CNT = 0;
                DSPLY '------  Delete  ------';
                Setll *Loval ORDSUM;
                Dou NOT %EOF(ORDSUM);
                    Read ORDSUM;
                    If NOT %EOF(ORDSUM);
                        delete ORDSUM;
                        CNT+=1;
                    EndIf;
                EndDO;
                DSPLY 'DELETED: ' + CNT +  ' RECORDS';
            EndSr;