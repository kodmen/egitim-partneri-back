import { IUser } from 'app/entities/user/user.model';
import { IKayit } from 'app/entities/kayit/kayit.model';
import { IRozet } from 'app/entities/rozet/rozet.model';

export interface IOgrenci {
  id?: number;
  level?: number | null;
  aciklama?: string | null;
  toplamPuan?: number | null;
  studentUser?: IUser | null;
  kayitlar2s?: IKayit[] | null;
  rozetlers?: IRozet[] | null;
}

export class Ogrenci implements IOgrenci {
  constructor(
    public id?: number,
    public level?: number | null,
    public aciklama?: string | null,
    public toplamPuan?: number | null,
    public studentUser?: IUser | null,
    public kayitlar2s?: IKayit[] | null,
    public rozetlers?: IRozet[] | null
  ) {}
}

export function getOgrenciIdentifier(ogrenci: IOgrenci): number | undefined {
  return ogrenci.id;
}
