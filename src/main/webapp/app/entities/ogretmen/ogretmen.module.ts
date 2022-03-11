import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OgretmenComponent } from './list/ogretmen.component';
import { OgretmenDetailComponent } from './detail/ogretmen-detail.component';
import { OgretmenUpdateComponent } from './update/ogretmen-update.component';
import { OgretmenDeleteDialogComponent } from './delete/ogretmen-delete-dialog.component';
import { OgretmenRoutingModule } from './route/ogretmen-routing.module';

@NgModule({
  imports: [SharedModule, OgretmenRoutingModule],
  declarations: [OgretmenComponent, OgretmenDetailComponent, OgretmenUpdateComponent, OgretmenDeleteDialogComponent],
  entryComponents: [OgretmenDeleteDialogComponent],
})
export class OgretmenModule {}
