import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImageModelComponent } from './list/image-model.component';
import { ImageModelDetailComponent } from './detail/image-model-detail.component';
import { ImageModelUpdateComponent } from './update/image-model-update.component';
import { ImageModelDeleteDialogComponent } from './delete/image-model-delete-dialog.component';
import { ImageModelRoutingModule } from './route/image-model-routing.module';

@NgModule({
  imports: [SharedModule, ImageModelRoutingModule],
  declarations: [ImageModelComponent, ImageModelDetailComponent, ImageModelUpdateComponent, ImageModelDeleteDialogComponent],
  entryComponents: [ImageModelDeleteDialogComponent],
})
export class ImageModelModule {}
