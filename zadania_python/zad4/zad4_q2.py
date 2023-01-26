import threading
import pika
import multiprocessing
import time

def reciever():
    
    queue = "queue1"
    
    credentials = pika.PlainCredentials('wdprir', 'wdprir')
    connection_parameters = pika.ConnectionParameters('54.37.137.241', virtual_host = 'wdprir', credentials = credentials)
    connection = pika.BlockingConnection(connection_parameters)

    channel = connection.channel()

    channel.queue_declare(queue)

    def callback(channel, method, properties, body):
        print(f'Received: {body.decode()}')

    #channel.basic_qos(prefetch_count=1)
    channel.basic_consume(queue, callback, auto_ack=False)
    channel.start_consuming()

    channel.close()


def sender():
    
    queue = "queue2"
    
    credentials = pika.PlainCredentials('wdprir', 'wdprir')
    connection_parameters = pika.ConnectionParameters('54.37.137.241', virtual_host = 'wdprir', credentials = credentials)
    connection = pika.BlockingConnection(connection_parameters)

    channel = connection.channel()

    channel.queue_declare(queue)
    
    while(True):
        mess = input('Enter message: ')
        print("You typed: " + mess)
        channel.basic_publish("", queue, mess)
        
    mess = input("Enter message: ")
    channel.basic_publish("", queue, mess)
    
    # while(True):
    #     channel.basic_publish("", queue, "abc")
    #     time.sleep(1)
    
    channel.close()

if __name__ == '__main__':

    #queue = "queue1"
    rec = threading.Thread(target=reciever)
    #sen = threading.Thread(target=sender)
    rec.start()
    sender()
    # rec = multiprocessing.Process(target=reciever)
    # sen = multiprocessing.Process(target=sender)
    
    sen.start()
    rec.start()
    
    sen.join()
    rec.join()

