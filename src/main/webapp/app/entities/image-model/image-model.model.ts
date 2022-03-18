export interface IImageModel {
  id?: number;
  name?: string | null;
  type?: string | null;
  imgContentType?: string | null;
  img?: string | null;
}

export class ImageModel implements IImageModel {
  constructor(
    public id?: number,
    public name?: string | null,
    public type?: string | null,
    public imgContentType?: string | null,
    public img?: string | null
  ) {}
}

export function getImageModelIdentifier(imageModel: IImageModel): number | undefined {
  return imageModel.id;
}
