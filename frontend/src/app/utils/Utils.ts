export class Utils {

  base58 = require('base-58');

  public parseInt(value: number): number{
    // TODO Refactor previous developer source code
    return Number(value.toFixed(15).substring(0, value.toFixed(15).indexOf('.')));
  }

  public parseFraction(value: number): number {
    // TODO Refactor previous developer source code
    let fraction = '';
    if(value.toString().indexOf('.') > 0) {
      let decimals = value - Math.floor(value);
      var decimalPlaces = value.toString().split('.')[1].length;
      let decimal = decimals.toFixed(decimalPlaces);
      if(value > 1) {
        fraction = (parseFloat(decimal) * 1000000000000000).toString() + '000'
      } else {
        fraction = (value * 1000000000000000000).toString()
      }
      fraction = fraction.replace(fraction.substring(fraction.length - 2, fraction.length), '00');
    }
    return Number(fraction);
  }

  public createTransactionBuffer(
    innerId: number,
    sourceBase58: string,
    targetBase58: string,
    amountInt: number,
    amountFrac: number,
    offeredMaxFeeInt: number,
    offeredMaxFeeFrac: number,
    currency: number,
    userFieldsCount: number,
    smartContractDataBase58: string
  ): Buffer {
    var buffer01 = Buffer.alloc(8);
    buffer01.writeIntLE(innerId, 0, 8);
    var sourceBuffer = new Buffer(this.base58.decode(sourceBase58));
    var targetBuffer = new Buffer(this.base58.decode(targetBase58));
    var buffer02 = Buffer.alloc(29);
    buffer02.writeIntLE(amountInt, 0, 4);
    buffer02.writeIntLE(amountFrac, 4, 8);
    buffer02.writeIntLE(offeredMaxFeeInt, 12, 4);
    buffer02.writeIntLE(offeredMaxFeeFrac, 16, 8);

    buffer02.writeIntLE(currency, 24, 1);
    buffer02.writeIntLE(userFieldsCount, 25, 4);
    var buffer = Buffer.concat([buffer01, sourceBuffer, targetBuffer, buffer02]);

    if (userFieldsCount > 0) {
      var smartContractDataBuffer = new Buffer(this.base58.decode(smartContractDataBase58));
      var buffer04 = Buffer.alloc(4);
      buffer04.writeIntLE(smartContractDataBuffer.byteLength, 0, 4);
      var buffer05 = Buffer.concat([buffer, buffer04, smartContractDataBuffer]);
      return buffer05;
    }
    return buffer;
  }

}
