import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Account{

    private int balance = 0;
    Lock lock;
    Condition condition;
    Condition prefferedCondition;

    void deposit(int k){
        this.lock.lock();
        try{
            this.balance += k;
            this.prefferedCondition.signal();
            this.condition.signallAll();
        }
        finally{
            this.lock.unlock();
        }
    }

    void prefferedWithdraw(int k){
        this.lock.lock();
        try {
            if(balance >= k){
                this.balance -= k;
            }
            else{
                //wait
                while(balance < k){
                    this.prefferedCondition.await();
                }
                prefferedWithdraw(k);
            }
        }
        finally {
            this.lock.unlock();
        }

    }

    void withdraw(int k){
        this.lock.lock();
        try {
            if(balance >= k){
                this.balance -= k;
            }
            else{
                //wait
                while(balance < k){
                    this.condition.await();
                }
                withdraw(k);
            }
        }
        finally{
            this.lock.unlock();
        }

    }
    void transfer(int k, Account reserve) {
        this.lock.lock();
        try{
            reserve.withdraw(k);
            deposit(k);
        }
        finally{
            this.lock.unlock();
        }
    }

}
